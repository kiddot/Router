package com.android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kiddo on 17-8-6.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    String path();
}
