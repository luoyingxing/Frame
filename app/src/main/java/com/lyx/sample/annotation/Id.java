package com.lyx.sample.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id
 * <p>
 * Created by luoyingxing on 2017/6/2.
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    //id is view's resource id ,default -1.
    int id() default -1;

    //is true , this view will setListener with View.OnTouchListener
    boolean onTouch() default false;

    //is true , this view will setListener with View.OnClickListener
    boolean onClick() default false;
}