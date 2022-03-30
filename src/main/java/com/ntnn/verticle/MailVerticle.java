package com.ntnn.verticle;

import com.ntnn.common.Utils;
import com.ntnn.repository.SendMail;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class MailVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MailVerticle.class);

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("process-mail", handler -> {
            String fileUploaded = (String) handler.body();
            log.info("Processing with file: " + fileUploaded);
            try {
                JSONArray jsonArray = Utils.parseJsonArray(fileUploaded);
                for(Object jsonObject : jsonArray) {
                    JSONObject obj = (JSONObject) jsonObject;
                    SendMail.getInstance().sendMail(
                            "nam0108ht@gmail.com",
                            obj.get("subject").toString(),
                            obj.get("to").toString(),
                            obj.get("body").toString()
                    );
                    log.info("Sent to: " + obj.toJSONString());
                }
            } catch (IOException | ParseException e) {
                log.error(e);
            }
        });
        super.start();
    }
}
