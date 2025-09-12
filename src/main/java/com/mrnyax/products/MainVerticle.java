package com.mrnyax.products;

import com.mrnyax.products.verticles.WebVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends VerticleBase {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public Future<?> start() {
    return configFileExists()
      .compose(this::initConfig)
      .compose(this::mergeCliConfig)
      .compose(this::deployVerticles);
  }

  private Future<JsonObject> mergeCliConfig(final JsonObject config) {
    final JsonObject cliConfig = config();
    LOG.info("Current config: {}", config);
    LOG.info("cli config: {}", cliConfig);
    final JsonObject mergedCliConfig = config.mergeIn(cliConfig);
    LOG.info("Merged cli config: {}", mergedCliConfig);
    return Future.succeededFuture(mergedCliConfig);
  }

  private Future<Boolean> configFileExists() {
    return vertx.fileSystem().exists("./config.json");
  }

  private Future<JsonObject> initConfig(boolean hasJsonConfigFile) {

    final ConfigRetrieverOptions configOpts = new ConfigRetrieverOptions();

    if (hasJsonConfigFile) {
      configOpts.addStore(initConfigFileWatcher());
    }

    if (System.getenv().containsKey("KUBERNETES_NAMESPACE")) {
      configOpts.addStore(initKubernetesWatcher());
    }

    ConfigRetriever configRetriever = ConfigRetriever
      .create(vertx, configOpts);

    return configRetriever.getConfig();
  }

  private ConfigStoreOptions initConfigFileWatcher() {
    LOG.info("Initializing config file watcher");
    return new ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(new JsonObject().put("path", "config.json"));
  }

  private ConfigStoreOptions initKubernetesWatcher() {
    LOG.info("Initializing Kubernetes config watcher");
    final String namespace = System.getenv().getOrDefault("KUBERNETES_NAMESPACE", "default");
    final JsonObject config = new JsonObject()
      .put("namespace", namespace)
      .put("name", "my-demo");
    return new ConfigStoreOptions()
      .setType("configMap")
      .setConfig(config);
  }

  private Future<?> deployVerticles(final JsonObject config) {
    final DeploymentOptions deploymentOptions = new DeploymentOptions()
      .setConfig(config);
    return vertx.deployVerticle(WebVerticle.class.getName(), deploymentOptions);
  }
}
