package com.leo.magic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.leo.magic.ThreadMode;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MagicThread {
    int delayMillisecond() default 0;

    ThreadMode threadMode() default ThreadMode.IO;
}
