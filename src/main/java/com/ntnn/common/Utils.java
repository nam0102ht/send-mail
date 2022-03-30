package com.ntnn.common;

import com.ntnn.model.User;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static JSONObject readFile(String path) throws IOException, ParseException {
        log.info("Processing file: " + path);
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            return (JSONObject) parser.parse(reader);
        }
    }

    public static JSONArray parseJsonArray(String path) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            return (JSONArray) parser.parse(reader);
        }
    }

    public static List<String> getListPattern(String text) {
        Pattern pattern = Pattern.compile("\\w+\\w-|\\{\\{\\w+\\}\\}");
        List<String> lst = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            lst.add(matcher.group());
        }
        return lst;
    }

    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date();
        return formatter.format(date);
    }
}
