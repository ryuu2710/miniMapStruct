package com.ryu.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // This sticky note only goes on variables
@Retention(RetentionPolicy.RUNTIME) // DO NOT throw this away when running!
public @interface MapFrom {
    String value(); // This will hold the source field name (e.g., "first_name")
}
