package com.example.demo;

import java.util.Map;
import org.springframework.boot.json.GsonJsonParser;

public class CommonUtils {
    
    public static Map<String, Object> parseJson(String jsonString) {
        return new GsonJsonParser().parseMap(jsonString);
    }

}
