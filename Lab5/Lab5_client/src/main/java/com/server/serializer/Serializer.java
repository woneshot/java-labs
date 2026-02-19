
package com.server.serializer;

import com.google.gson.Gson;

public class Serializer {
    private static final Gson gson = new Gson();

    public static String serialize(Object object) {
        return gson.toJson(object);
    }
}