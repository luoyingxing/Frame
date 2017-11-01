package com.lyx.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id
 * 该注解用于标注View组件，传入View的资源id，省去 findViewById() 这个麻烦的操作，该注解配合{IdParser.class}使用
 * <p/>
 * author:  luoyingxing
 * date: 2017/10/16.
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    int value() default -1;
}