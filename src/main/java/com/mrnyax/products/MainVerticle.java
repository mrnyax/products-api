package com.mrnyax.products;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends VerticleBase {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public Future<?> start() {
    return vertx.createHttpServer()
      .requestHandler(req ->
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!"))
      .listen(8888)
      .onSuccess(http -> LOG.info("HTTP server started on port 8888"));
  }
}
