# Products REST API

[![Vert.x](https://img.shields.io/badge/vert.x-5.0.4-purple.svg)](https://vertx.io)

This application was generated using [start.vertx.io](http://start.vertx.io).

## Building

To run tests:
```bash
  ./mvnw clean test
```

To package application:
```bash
  ./mvnw clean package
```

To run application:
```bash
  ./mvnw clean compile exec:java
```

To run, redeploy and test without repackaging:
```bash
  ./mvnw clean vertx:run
```

To build a local docker image with image `products-api:latest`:
```bash
  ./mvnw clean compile jib:dockerBuild
```

To build a local docker image with custom image name and tag:
```bash
  ./mvnw clean compile jib:dockerBuild -Dimage={image-name}:{image-tag}
```

## Need help?

* [Vert.x Documentation](https://vertx.io/docs/)
* [Vert.x Stack Overflow](https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15)
* [Vert.x User Group](https://groups.google.com/forum/?fromgroups#!forum/vertx)
* [Vert.x Discord](https://discord.gg/6ry7aqPWXy)
