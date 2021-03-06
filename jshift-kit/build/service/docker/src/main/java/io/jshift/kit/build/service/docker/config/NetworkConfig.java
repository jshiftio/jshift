package io.jshift.kit.build.service.docker.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Network config encapsulating network specific configuration
 * @author roland
 * @since 29/07/16
 */

public class NetworkConfig implements Serializable {

    private String name;

    private Mode mode;

    private List<String> aliases = new ArrayList<>();

    // Use by Maven to add flattened <alias> entries
    // See  http://blog.sonatype.com/2011/03/configuring-plugin-goals-in-maven-3/
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * Legacy constructor using the ;&lt;net;&gt; config
     * @param net net, encapsulating mode and name.
     */
    public NetworkConfig(String net) {
        initLegacyNetSpec(net);
    }


    public NetworkConfig(Mode mode, String name) {
        this.name = name;
        this.mode = mode;
    }

    public NetworkConfig() {
        name = null;
        mode = null;
    }

    private void initLegacyNetSpec(String net) {
        if (net != null) {
            this.mode = extractMode(net);
            if (this.mode == Mode.container) {
                this.name = net.substring(Mode.container.name().length() + 1);
            } else if (this.mode == Mode.custom) {
                this.name = net;
            } else {
                this.name = null;
            }
        } else {
            this.mode = null;
            this.name = null;
        }
    }

    private Mode extractMode(String mode) {
        if (mode != null && mode.length() > 0) {
            try {
                return Mode.valueOf(mode.toLowerCase());
            } catch (IllegalArgumentException exp) { /* could be a custom mode, too */ }
            if (mode.toLowerCase().startsWith(Mode.container.name() + ":")) {
                return Mode.container;
            } else {
                return Mode.custom;
            }
        }
        return null;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isCustomNetwork() {
        return (mode != null && mode == Mode.custom) || (mode == null && name != null);
    }

    public boolean isStandardNetwork() {
        return mode != null && mode != Mode.custom;
    }

    public String getStandardMode(String containerId) {
        if (isCustomNetwork()) {
            throw new IllegalArgumentException("Custom network for network '" + name +
                    "' can not be used as standard mode");
        }
        if (mode == null) {
            return null;
        }
        return mode.name().toLowerCase() + (mode == Mode.container ? ":" + containerId : "");
    }

    public String getContainerAlias() {
        return mode == Mode.container ? name : null;
    }

    public String getCustomNetwork() {
        return mode == Mode.custom || mode == null ? name : null;
    }

    public boolean hasAliases() {
        return aliases != null && !aliases.isEmpty();
    }

    public Mode getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }

    // ==============================================================================

    // Mode used for determining the network
    public enum Mode {
        none,
        bridge,
        host,
        container,
        custom;
    }

    public static class Builder {

        private final NetworkConfig config = new NetworkConfig();
        private boolean isEmpty = true;

        public NetworkConfig build() {
            return isEmpty ? null : config;
        }

        public Builder mode(String mode) {
            config.mode = set(mode != null ? Mode.valueOf(mode) : null);
            return this;
        }

        public Builder name(String name) {
            config.name = set(name);
            return this;
        }


        public Builder aliases(List<String> aliases) {
            config.aliases = set(aliases);
            return this;
        }

        private <T> T set(T prop) {
            if (prop != null) {
                isEmpty = false;
            }
            return prop;
        }
    }
}

