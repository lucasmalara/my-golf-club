package com.mygolfclub.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectMember {
    String firstName() default "Lorem";

    String lastName() default "Ipsum";

    String email() default "dolor@sit.amet";

    boolean active() default true;
}
