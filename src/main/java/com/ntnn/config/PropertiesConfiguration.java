package com.ntnn.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class PropertiesConfiguration {

    private static final PropertiesConfiguration INSTANCE = new PropertiesConfiguration();

    public PropertiesConfiguration() {}

    public static PropertiesConfiguration getInstance() {
        return INSTANCE;
    }
    
    public ConfigRetriever config(Vertx vertx, String path) {
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject()
                        .put("path", path)
                );

        return ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));
    }
}
