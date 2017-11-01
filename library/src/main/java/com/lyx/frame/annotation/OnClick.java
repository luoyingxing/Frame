package com.lyx.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OnClick
 * <p>
 * 该注解用于标注View组件，快速实现监听事件，该注解配合{IdParser.class}使用
 * Autor: luoyingxing
 * Time: 2017/10/22 0022
 */

@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick {
}