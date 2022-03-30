package com.ntnn.handler;

import com.ntnn.model.User;

@FunctionalInterface
public interface Handler {
    String handler(User user, String body);
}
