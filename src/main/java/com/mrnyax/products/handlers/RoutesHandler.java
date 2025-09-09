package com.mrnyax.products.handlers;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.UUID;

public final class RoutesHandler {

  private RoutesHandler() {
  }

  public static void add(RoutingContext routingContext) {
    final JsonObject responseBody = new JsonObject();
    responseBody.put("status", "created");
    toJsonResponse(routingContext, 201, responseBody);
  }

  public static void get(final RoutingContext routingContext) {
    final JsonObject responseBody = new JsonObject();
    responseBody.put("name", "Kiwi");
    responseBody.put("id", routingContext.request().getParam("id"));
    toJsonResponse(routingContext, 200, responseBody);
  }

  public static void list(final RoutingContext routingContext) {
    final JsonArray responseBody = new JsonArray();
    responseBody.add(new JsonObject().put("id", UUID.randomUUID().toString()).put("name", "Kiwi"));
    toJsonResponse(routingContext, 200, responseBody);
  }

  public static void update(RoutingContext routingContext) {
    final JsonObject responseBody = new JsonObject();
    responseBody.put("name", "Omo");
    responseBody.put("id", routingContext.request().getParam("id"));
    toJsonResponse(routingContext, 200, responseBody);
  }

  public static void delete(RoutingContext routingContext) {
    toJsonResponse(routingContext, 204, null);
  }

  private static void toJsonResponse(RoutingContext routingContext, int statusCode, Object body) {
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(statusCode)
      .end(
        body instanceof JsonObject jo ? jo.encode()
          : body instanceof JsonArray ja ? ja.encode()
          : String.valueOf(body)
      );
  }
}
