package com.ntnn.handler;

import com.ntnn.common.Utils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class StrategyHandlerMapping {
    private final Map<String, Handler> map = new HashMap<>();
    public StrategyHandlerMapping() {
        map.put("{{TITLE}}", (user, body) -> body.replace("{{TITLE}}", user.getTitle()));
        map.put("{{FIRSTNAME}}", (user, body) -> body.replace("{{FIRSTNAME}}", user.getFirstName()));
        map.put("{{LASTNAME}}", (user, body) -> body.replace("{{LASTNAME}}", user.getLastName()));
        map.put("{{TODAY}}", (user, body) -> body.replace("{{TODAY}}", Utils.getDate()));
    }
}
