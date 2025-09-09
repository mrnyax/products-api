package com.mrnyax.products.verticles;

import com.mrnyax.products.handlers.RoutesHandler;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class WebVerticle extends VerticleBase {

  @Override
  public Future<?> start() {
    return configureRoutes()
      .compose(this::startHttpServer);
  }

  private Future<Router> configureRoutes() {
    final Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post("/products").handler(RoutesHandler::add);
    router.get("/products/:id").handler(RoutesHandler::get);
    router.get("/products").handler(RoutesHandler::list);
    router.put("/products/:id").handler(RoutesHandler::update);
    router.delete("/products/:id").handler(RoutesHandler::delete);
    return Future.succeededFuture(router);
  }

  private Future<HttpServer> startHttpServer(final Router router) {
    final HttpServer server = vertx.createHttpServer();
    server.requestHandler(router);
    int port = getServerPort();
    return server
      .listen(port)
      .onSuccess(r -> {})
      .onFailure(err -> {});
  }

  private static int getServerPort() {
    return 8000;
  }
}
