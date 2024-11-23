package com.example.quay_so.utils.validate;

import com.google.gson.Gson;

public class ConvertUtils {
    private static final Gson gson = new Gson();

    // Chuyển đối tượng thành chuỗi JSON
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    // Chuyển từ chuỗi JSON thành đối tượng
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
