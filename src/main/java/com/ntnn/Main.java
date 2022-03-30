package com.ntnn;

import com.ntnn.common.Utils;
import com.ntnn.handler.Handler;
import com.ntnn.handler.StrategyHandlerMapping;
import com.ntnn.model.User;
import com.ntnn.repository.ReadCsvFile;
import com.ntnn.verticle.MailVerticle;
import com.ntnn.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    private static final Main INSTANCE = new Main();

    public static Main getInstance() {
        return INSTANCE;
    }

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            Main.getInstance().processVertx();
        } else {
            Main.getInstance().processCommandLine(args);
        }
    }

    public void processVertx() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
        vertx.deployVerticle(new MailVerticle());
    }

    public void processCommandLine(String[] args) {
        final String pathTemplate = args[0];
        if ("".equalsIgnoreCase(pathTemplate)) {
            log.info("Please add pathTemplate in command");
            return;
        }
        final String data = args[1];
        if ("".equalsIgnoreCase(data)) {
            log.info("Please add data in command");
            return;
        }
        final String outJson = args[2];
        if ("".equalsIgnoreCase(outJson)) {
            log.info("Please add outputFilePath in command");
            return;
        }
        final String errorPath = args[3];
        if ("".equalsIgnoreCase(errorPath)) {
            log.info("Please add errorPath in command");
            return;
        }
        try {
            JSONObject objectTemplate = Utils.readFile(pathTemplate);
            final String bodyTemplate = (String) objectTemplate.get("body");
            if ("".equalsIgnoreCase(bodyTemplate)) {
               log.info("Body of template is empty");
                return;
            }
            final List<String> valueTemplate = Utils.getListPattern(bodyTemplate);
            final List<User> lstUser = ReadCsvFile.getInstance().readFromFile(data);
            final JSONArray jsonArray = new JSONArray();
            final AtomicReference<List<User>> atomicErrors = new AtomicReference<>();
            atomicErrors.set(new ArrayList<>());
            final AtomicReference<String> atomicBody = new AtomicReference<>();
            atomicBody.set(bodyTemplate);
            StrategyHandlerMapping mapsStrategy = new StrategyHandlerMapping();
            final Map<String, Handler> maps = mapsStrategy.getMap();
            log.info("Starting process");
            CompletableFuture<Void> completeProcessMapping = CompletableFuture.runAsync(() -> {
                log.info("Please wait, just a second it'll be done");
                for (int j = 1; j < lstUser.size(); j++) {
                    User v = lstUser.get(j);
                    if("".equalsIgnoreCase(v.getEmail())) {
                        atomicErrors.get().add(v);
                    } else {
                        String body = atomicBody.get();
                        for (int i = 0; i < valueTemplate.size(); i++) {
                            String template = valueTemplate.get(i);
                            Handler handler = maps.getOrDefault(template, null);
                            if(handler == null) {
                                log.info("Check again Title of CSV data input file");
                                return;
                            }
                            body = handler.handler(v, body);
                        }
                        if (body.equalsIgnoreCase(atomicBody.get())) {
                            log.info("Dealing with issue when parsing, please check data input, template mail,...");
                            return;
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", objectTemplate.get("from"));
                        jsonObject.put("to", v.getEmail());
                        jsonObject.put("subject", objectTemplate.get("subject"));
                        jsonObject.put("mimeType", "text/plain");
                        jsonObject.put("body", body);
                        jsonArray.add(jsonObject);
                        atomicBody.set(bodyTemplate);
                    }
                }
                log.info("Completing mapped data from template with data input");
            });
            completeProcessMapping.join();
            CompletableFuture<Void> saveFile = completeProcessMapping.thenRunAsync(() -> {
                try {
                    ReadCsvFile.getInstance().saveFileJson(jsonArray, outJson);
                    ReadCsvFile.getInstance().saveErrors(atomicErrors.get(), errorPath);
                } catch (IOException e) {
                    log.error(e);
                }

            });
            saveFile.join();
        } catch(IOException | ParseException e) {
            log.error(e);
        }
    }

}
