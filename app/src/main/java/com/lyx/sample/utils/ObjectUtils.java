package com.lyx.sample.utils;


import com.lyx.frame.annotation.Renewal;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ObjectUtils
 * <p>
 * Created by luoyingxing on 2017/7/29.
 */

public class ObjectUtils {

    public static <T> Map<String, Object> erasureUnmodified(T oldObj, T newObj) {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = oldObj.getClass();
        Field[] newFields = newObj.getClass().getDeclaredFields();
        for (Field newField : newFields) {
            if (newField.isAnnotationPresent(Renewal.class)) {
                String fieldName = newField.getName();
                String classType = newField.getType().toString();
                int lastIndex = classType.lastIndexOf(".");
                classType = classType.substring(lastIndex + 1);

                try {
                    if (classType.equals("boolean")) {
                        String isName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Object oldValue = clazz.getMethod(isName).invoke(oldObj);
                        Object newValue = clazz.getMethod(isName).invoke(newObj);
                        if (!equals(oldValue, newValue)) {
                            map.put(fieldName, newValue);
                        }
                    } else {
                        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Object oldValue = clazz.getMethod(getterName).invoke(oldObj);
                        Object newValue = clazz.getMethod(getterName).invoke(newObj);
                        if (!equals(oldValue, newValue)) {
                            map.put(fieldName, newValue);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public static <T> Map<String, Object> erasureUnmodified2(T oldObj, T newObj) {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> oldClass = oldObj.getClass();
        Field[] newFields = newObj.getClass().getDeclaredFields();
        for (Field newField : newFields) {
            if (newField.isAnnotationPresent(Renewal.class)) {
                try {
                    Field oldField = oldClass.getDeclaredField(newField.getName());
                    oldField.setAccessible(true);
                    newField.setAccessible(true);
                    Object newValue = newField.get(newObj);
                    if (!equals(oldField.get(oldObj), newValue)) {
                        map.put(newField.getName(), newValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}