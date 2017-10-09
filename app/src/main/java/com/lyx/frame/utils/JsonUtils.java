package com.lyx.frame.utils;


import com.lyx.frame.annotation.Renewal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JsonUtils
 * <p>
 * Created by luoyingxing on 2017/7/29.
 */

public class JsonUtils {

    public static <T> Map<String, Object> toJsonMap(T obj) {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Renewal.class)) {
                String fieldName = field.getName();
                String classType = field.getType().toString();
                int lastIndex = classType.lastIndexOf(".");
                classType = classType.substring(lastIndex + 1);

                try {
                    if (classType.equals("boolean")) {
                        String isName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Object value = clazz.getMethod(isName).invoke(obj);
                        map.put(fieldName, value);
                    } else {
                        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Object value = clazz.getMethod(getterName).invoke(obj);
                        map.put(fieldName, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public static String toJsonString(Map<String, Object> map) {
        return null;
    }
}
