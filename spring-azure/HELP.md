# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.8/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.8/gradle-plugin/packaging-oci-image.html)
* [Azure Actuator](https://aka.ms/spring/docs/actuator)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/3.3.8/reference/testing/testcontainers.html#testing.testcontainers)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.3.8/reference/actuator/index.html)
* [Azure Cosmos DB](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#spring-data-support)
* [Azure Storage](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#resource-handling)
* [Testcontainers](https://java.testcontainers.org/)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.8/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [How to use Spring Boot Starter with Azure Cosmos DB SQL API](https://aka.ms/spring/msdocs/cosmos)
* [How to use the Spring Boot starter for Azure Storage](https://aka.ms/spring/msdocs/storage)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Azure Cosmos DB Sample](https://aka.ms/spring/samples/latest/cosmos)
* [Azure Storage Sample](https://aka.ms/spring/samples/latest/storage)

### Testcontainers support

This project
uses [Testcontainers at development time](https://docs.spring.io/spring-boot/3.3.8/reference/features/dev-services.html#features.dev-services.testcontainers).

Testcontainers has been configured to use the following Docker images:

* [`mcr.microsoft.com/azure-storage/azurite:latest`](https://github.com/Azure/Azurite?tab=readme-ov-file#dockerhub)

Please review the tags of the used images and set them to the same as you're running in production.

