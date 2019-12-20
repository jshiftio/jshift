# Jshift

> *Note: This repository has been moved to [Eclipse Jkube](https://github.com/eclipse/jkube). In case you want to contribute please send PRs to https://github.com/eclipse/jkube repo*.

[![Circle CI](https://circleci.com/gh/jshiftio/jshift/tree/master.svg?style=shield)](https://circleci.com/gh/jshiftio/jshift-kit/tree/master)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=jshiftio_jshift&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=jshiftio_jshift)
[![Gitter](https://badges.gitter.im/jshift-community/community.svg)](https://gitter.im/jshift-community/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jshiftio_jshift&metric=coverage)](https://sonarcloud.io/dashboard?id=jshiftio_jshift)

This project contains various building blocks for the jshift developer toolbox.

Actually it contains the following abstractions which has been extracted from both projects:

* **Kubernetes Maven Plugin** <br/>
[![Maven Central](https://img.shields.io/maven-central/v/io.jshift/k8s-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jshift%22%20AND%20a:%22k8s-maven-plugin%22)

* **Openshift Maven Plugin** <br/> 
[![Maven Central](https://img.shields.io/maven-central/v/io.jshift/oc-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jshift%22%20AND%20a:%22oc-maven-plugin%22)

* **Jshift-kit**, which consists of the following:

  * **Generator** framework for automatically generating Docker images by examining project information.<br />
  [![Maven Central](https://img.shields.io/maven-central/v/io.jshift/jshift-maven-generator-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jshift%22%20AND%20a:%22jshift-maven-generator-api%22) [![Javadocs](http://www.javadoc.io/badge/io.jshift/jshift-maven-generator-api.svg?color=blue)](http://www.javadoc.io/doc/io.jshift/jshift-maven-generator-api)
  * **Enricher** framework for creating and enhancing Kubernetes and OpenShift resources.<br />
  [![Maven Central](https://img.shields.io/maven-central/v/io.jshift/jshift-maven-enricher-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jshift%22%20AND%20a:%22jshift-maven-enricher-api%22) [![Javadocs](http://www.javadoc.io/badge/io.jshift/jshift-maven-enricher-api.svg?color=blue)](http://www.javadoc.io/doc/io.jshift/jshift-maven-enricher-api)
  * **Profile** combining the configuration for generators and enrichers.<br />
  [![Maven Central](https://img.shields.io/maven-central/v/io.jshift/jshift-maven-profiles.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jshift%22%20AND%20a:%22jshift-maven-profiles%22) [![Javadocs](http://www.javadoc.io/badge/io.jshift/jshift-maven-profiles.svg?color=blue)](http://www.javadoc.io/doc/io.jshift/jshift-maven-profiles)
  * **Resource configuration** model objects for a simplified configuration of Kubernetes and OpenShift resources.<br />
  [![Maven Central](https://img.shields.io/maven-central/v/io.jshift/jshift-kit-config-resource.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jshift%22%20AND%20a:%22jshift-kit-config-resource%22) [![Javadocs](http://www.javadoc.io/badge/io.jshift/jshift-kit-config-resource.svg?color=blue)](http://www.javadoc.io/doc/io.jshift/jshift-kit-config-resource)
  * **Image configuration** model objects for modeling Docker image configuration as used in docker-maven-plugin.<br />
  [![Maven Central](https://img.shields.io/maven-central/v/io.jshift/jshift-kit-config-image.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jshift%22%20AND%20a:%22jshift-kit-config-image%22) [![Javadocs](http://www.javadoc.io/badge/io.jshift/jshift-kit-config-image.svg?color=blue)](http://www.javadoc.io/doc/io.jshift/jshift-kit-config-image)

One intention of extracting these parts from the originating plugins is also to separate Maven related and non-Maven related functionality so that the non-Maven parts can be reused for other build systems and IDEs like Gradle or Eclipse. Some thin adapter Maven specific modules like [jshift-kit-enricher-maven](enricher/maven/pom.xml)  and [jshift-kit-generator-maven](generator/maven/pom.xml) are provided as glue to get to the Maven specific build information like the project's coordinates.


By moving out common parts it will be now also be possible for the [docker-maven-plugin](https://github.com/fabric8io/docker-maven-plugin) to benefit from the generator framework for zero-config creation of Docker images.


<div style="text-align:center"><img src ="https://i.imgur.com/1IBIDgB.jpg" /></div>

