package com.ntnn.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SendMailHandler implements Handler<RoutingContext> {
    private Vertx vertx;

    private static final Logger log = LoggerFactory.getLogger(SendMailHandler.class);

    @Override
    public void handle(RoutingContext context) {
        JsonArray jsonArray = new JsonArray();
        String fileName = "";
        JsonObject obj;
        EventBus eventBus = vertx.eventBus();
        for (FileUpload response : context.fileUploads()) {
            obj = new JsonObject();
            obj.put("status", "200");
            obj.put("message", "saved success");
            fileName = response.uploadedFileName();
            jsonArray.add(obj);
            eventBus.send("process-mail", fileName);
        }
        obj = new JsonObject();
        obj.put("status", "Success");
        obj.put("message", jsonArray.toString());
        context.response().end(obj.toString());
    }
}
