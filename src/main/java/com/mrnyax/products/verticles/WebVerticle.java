package com.mrnyax.products.verticles;

import com.mrnyax.products.handlers.RoutesHandler;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebVerticle extends VerticleBase {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebVerticle.class);
  private static final int DEFAULT_PORT = 8080;

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
      .onSuccess(r -> LOGGER.info("HTTP server started on port {}", port))
      .onFailure(err -> LOGGER.error("HTTP server failed on port {}", port, err));
  }

  private int getServerPort() {
    final JsonObject serverConfig = config().getJsonObject("server");
    if (serverConfig == null) {
      LOGGER.error("No server config found.");
      return DEFAULT_PORT;
    }
    return serverConfig.getInteger("port", DEFAULT_PORT);
  }
}
