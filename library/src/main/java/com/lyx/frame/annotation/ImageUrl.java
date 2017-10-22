package com.lyx.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ImageUrl 该注解用于标注泛型类实体中图片的地址字段
 * <p>
 * Autor: luoyingxing
 * Time: 2017/10/22 0022
 */

@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageUrl {
}