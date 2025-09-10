package com.mrnyax.products.verticles;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class WebVerticleTest {

  @BeforeEach
  void setUp(final Vertx vertx, final VertxTestContext testContext) {
    vertx.deployVerticle(WebVerticle.class.getName())
      .onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void getProducts_whenOk_returnsListOfProducts(Vertx vertx, VertxTestContext testContext) {
    final WebClient webClient = WebClient.create(vertx);
    webClient.get(8000, "localhost", "/products")
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        assertNotNull(response.bodyAsJsonArray());
        JsonArray responseBody = response.bodyAsJsonArray();
        assertFalse(responseBody.isEmpty());
        JsonObject product = responseBody.getJsonObject(0);
        assertTrue(product.containsKey("id"));
        assertTrue(product.containsKey("name"));
        testContext.completeNow();
      })));
  }

  @Test
  void getProduct_whenOk_returnsProduct(Vertx vertx, VertxTestContext testContext) {
    final WebClient webClient = WebClient.create(vertx);
    final String productId = UUID.randomUUID().toString();
    webClient.get(8000, "localhost", "/products/" + productId)
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        assertNotNull(response.bodyAsJsonObject());
        JsonObject product = response.bodyAsJsonObject();
        assertTrue(product.containsKey("id"));
        assertTrue(product.containsKey("name"));
        testContext.completeNow();
      })));
  }

  @Test
  void createProduct_whenOk_returnsProductAndStatus201(Vertx vertx, VertxTestContext testContext) {
    final WebClient webClient = WebClient.create(vertx);
    webClient.post(8000, "localhost", "/products")
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(201, response.statusCode());
        assertNotNull(response.body());
        JsonObject jsonResponseBody = response.bodyAsJsonObject();
        assertTrue(jsonResponseBody.containsKey("id"));
        assertTrue(jsonResponseBody.containsKey("name"));
        testContext.completeNow();
      })));
  }

  @Test
  void updateProduct_whenOk_returnsProductAndStatus200(Vertx vertx, VertxTestContext testContext) {
    final WebClient webClient = WebClient.create(vertx);
    final String productId = UUID.randomUUID().toString();
    webClient.put(8000, "localhost", "/products/" + productId)
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        JsonObject jsonResponseBody = response.bodyAsJsonObject();
        assertTrue(jsonResponseBody.containsKey("id"));
        assertTrue(jsonResponseBody.containsKey("name"));
        testContext.completeNow();
      })));
  }

  @Test
  void deleteProduct_whenOk_returnsEmptyAndStatus204(Vertx vertx, VertxTestContext testContext) {
    final WebClient webClient = WebClient.create(vertx);
    final String productId = UUID.randomUUID().toString();
    webClient.delete(8000, "localhost", "/products/" + productId)
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(204, response.statusCode());
        assertNull(response.body());
        testContext.completeNow();
      })));
  }

}
