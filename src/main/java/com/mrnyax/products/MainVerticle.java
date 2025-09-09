package com.mrnyax.products;

import com.mrnyax.products.verticles.WebVerticle;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends VerticleBase {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public Future<?> start() {
    return deployVerticles();
  }

  private Future<?> deployVerticles() {
    return vertx.deployVerticle(WebVerticle.class.getName());
  }
}
