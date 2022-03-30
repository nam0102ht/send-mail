package com.ntnn.verticle;

import com.ntnn.config.PropertiesConfiguration;
import com.ntnn.handler.SendMailHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.lang.reflect.Field;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

    private static final String API_V2 = "/api/v2";
    private static final String POST_MAIL = API_V2 + "/send-mail";
    private static final String FILE_LOCATION = "src/main/resources/data";

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.post(POST_MAIL).handler(BodyHandler.create()
                .setUploadsDirectory(FILE_LOCATION));
        router.post(POST_MAIL)
                .handler(new SendMailHandler(vertx));
        ConfigRetriever configRetriever = PropertiesConfiguration.getInstance().config(vertx, "application.yml");
        configRetriever.getConfig(json -> {
            JsonObject result = json.result();

            setEnv("SENDGRID_API_KEY", result.getString("sendGridApi"));

            log.info("Server is established");

            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(result.getJsonObject("server").getInteger("port"), context -> {
                        if(context.succeeded()) {
                            log.info("Server is listening port: 8000");
                        }
                    });
        });
        super.start();
    }

    public void setEnv(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }
}
