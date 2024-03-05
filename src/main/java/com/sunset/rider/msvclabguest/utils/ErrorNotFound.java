package com.sunset.rider.msvclabguest.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public  class ErrorNotFound {

    public static Map<String,String> error(String id){
        Map<String,String> error = new HashMap<>();
        error.put("error","En nuestra base de datos no existe el id ".concat(id));
        error.put("timestamp", LocalDateTime.now().toString());
        return  error;
    }
}
