package io.jshift.kit.config.image.build;

/**
 * OpenShift build mode. Only used when mode is "openshift"
 *
 * @author roland
 * @since 01/08/16
 */
public enum OpenShiftBuildStrategy {

    // Constants used to extract extra information from a `fromExt` build configuration
    /**
     * S2i build with a binary source
     */
    s2i("S2I"),

    /**
     * Docker build with a binary source
     */
    docker("Docker");

    // Source strategy elemens
    public enum SourceStrategy {
        kind,
        namespace,
        name;

        public String key() {
            // Return the name, could be mapped if needed.
            return name();
        }
    }


    private final String label;

    private OpenShiftBuildStrategy(String label) {
        this.label = label;
    }

    /**
     * Check if the given type is same as the type stored in OpenShift
     *
     * @param type to check
     * @return boolean value whether type is same or not.
     */
    public boolean isSame(String type) {
        return type != null &&
                (type.equalsIgnoreCase("source") && this == s2i) ||
                (type.equalsIgnoreCase("docker") && this == docker);
    }

    public String getLabel() {
        return label;
    }
}

