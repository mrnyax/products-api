package com.mrnyax.products;

import com.mrnyax.products.verticles.WebVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @Test
  public void mainVerticlesIsDeployed(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle())
      .onComplete(testContext.succeeding(id -> {
        testContext.verify(() -> {
          assertNotNull(id);
          testContext.completeNow();
        });
      }));
  }

  @Test
  void mainVerticle_deploysWebVerticle() throws Exception {
    // Arrange
    Vertx mockVertx = mock(Vertx.class);
    when(mockVertx.deployVerticle(anyString()))
      .thenReturn(Future.succeededFuture("web-deploy-id"));

    MainVerticle main = new MainVerticle();
    main.init(mockVertx, null);

    // Act
    main.start();

    // Assert
    verify(mockVertx, times(1)).deployVerticle(eq(WebVerticle.class.getName()));
    verifyNoMoreInteractions(mockVertx);
  }
}
