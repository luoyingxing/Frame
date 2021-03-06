package com.lyx.frame.annotation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lyx.frame.exception.ParserException;

import java.lang.reflect.Field;

/**
 * IdParser
 * 该类用于处理标注View组件，通过反射实现findViewById()这个麻烦的操作，同时支持设置View的监听事件和触摸事件，配合{OnClick}{OnTouch}两个注解使用
 * <p/>
 * author:  luoyingxing
 * date: 2017/10/16.
 */
public class IdParser {
    /**
     * if it has been use in the activity, this method at least called in the OnCreate();
     * <p>
     * if it has been use in the fragment, this method at least called in the onViewCreated();
     * <p>
     *
     * @param object Activity , fragment or view
     */
    public static void inject(Object object) {
        try {
            parse(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parse(Object object) throws Exception {
        final Class<?> clazz = object.getClass();
        View view = null;
        //Traverse all the members of an object variable.
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                Id injectView = field.getAnnotation(Id.class);
                int id = injectView.value();

                boolean onTouch = field.isAnnotationPresent(OnTouch.class);
                boolean onClick = field.isAnnotationPresent(OnClick.class);

                if (id < 0) {
                    throw new ParserException("View id must not be null!");
                } else {
                    //if field is private,must setAccessible(true), then can access it
                    field.setAccessible(true);

                    if (object instanceof Activity) {
                        view = ((Activity) object).findViewById(id);

                        if (onClick) {
                            if (object instanceof View.OnClickListener) {
                                view.setOnClickListener((View.OnClickListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnClickListener!");
                            }
                        }

                        if (onTouch) {
                            if (object instanceof View.OnTouchListener) {
                                view.setOnTouchListener((View.OnTouchListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnTouchListener!");
                            }
                        }

                    } else if (object instanceof Fragment) {
                        if (((Fragment) object).getView() == null) {
                            throw new ParserException("Fragment's root view is null");
                        }
                        view = ((Fragment) object).getView().findViewById(id);

                        if (onClick) {
                            if (object instanceof View.OnClickListener) {
                                view.setOnClickListener((View.OnClickListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnClickListener!");
                            }
                        }

                        if (onTouch) {
                            if (object instanceof View.OnTouchListener) {
                                view.setOnTouchListener((View.OnTouchListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnTouchListener!");
                            }
                        }

                    } else if (object instanceof View) {
                        view = ((View) object).findViewById(id);
                    }

                    field.set(object, view);
                }
            }
        }
    }
}