/**
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package io.jshift.enricher.generic;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateBuilder;
import io.jshift.kit.build.service.docker.ImageConfiguration;
import io.jshift.kit.config.resource.PlatformMode;
import io.jshift.kit.config.resource.ProcessorConfig;
import io.jshift.maven.enricher.api.MavenEnricherContext;
import io.jshift.maven.enricher.api.model.Configuration;
import io.jshift.kit.common.util.ResourceUtil;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author dgaur
 * @since 03/06/16
 */
public class PortNameEnricherTest {

    @Mocked
    private MavenEnricherContext context;

    @Mocked
    ImageConfiguration imageConfiguration;

    @Test
    public void checkDefaultConfiguration() throws Exception {
        setupExpectations("type", "LoadBalancer");

        KubernetesList list = enrich();
        assertEquals(list.getItems().size(), 1);

        String json = ResourceUtil.toJson(list.getItems().get(0));
        assertThat(json, isJson());
        assertPort(json, 0, 80, 80, "http", "TCP");
    }

    private KubernetesList enrich() throws com.fasterxml.jackson.core.JsonProcessingException {
        // Enrich
        PortNameEnricher portNameEnricher = new PortNameEnricher(context);
        KubernetesListBuilder builder = getPodTemplateList();
        portNameEnricher.create(PlatformMode.kubernetes,builder);

        // Validate that the generated resource contains
        KubernetesList list = builder.build();
        System.out.println(list);

        return list;
    }

    private KubernetesListBuilder getPodTemplateList() {
        Container container = new ContainerBuilder()
                .withName("test-port-enricher")
                .withImage("test-image")
                .withPorts(new ContainerPortBuilder().withContainerPort(80).withProtocol("TCP").build())
                .build();
        PodTemplateBuilder ptb = new PodTemplateBuilder()
                .withNewMetadata().withName("test-pod")
                .endMetadata()
                .withNewTemplate()
                .withNewSpec()
                .withContainers(container)
                .endSpec()
                .endTemplate();
        return new KubernetesListBuilder().addToPodTemplateItems(ptb.build());
    }

    private void setupExpectations(String ... configParams) {
        setupExpectations(true, configParams);
    }

    private void setupExpectations(final boolean withPorts, String ... configParams) {
        // Setup mock behaviour
        final TreeMap config = new TreeMap();
        for (int i = 0; i < configParams.length; i += 2) {
            config.put(configParams[i],configParams[i+1]);
        }

        new Expectations() {{

            Configuration configuration = new Configuration.Builder()
                    .images(Arrays.asList(imageConfiguration))
                    .processorConfig(new ProcessorConfig(null, null, Collections.singletonMap("jshift-portname", config)))
                    .build();

            context.getConfiguration();
            result = configuration;
        }};
    }

    private void assertPort(String json, int idx, int port, int targetPort, String name, String protocol) {
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.template.spec.containers[0].ports[" + idx + "].containerPort", equalTo(port)));
        assertThat(json, hasJsonPath("$.template.spec.containers[0].ports[" + idx + "].name", equalTo(name)));
        assertThat(json, hasJsonPath("$.template.spec.containers[0].ports[" + idx + "].protocol", equalTo(protocol)));
    }
}
