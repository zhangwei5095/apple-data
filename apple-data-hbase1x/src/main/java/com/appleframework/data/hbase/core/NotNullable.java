package com.appleframework.data.hbase.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Flag to indicate the method's result or parameter or field cann't be null.
 * 
 * @author xinzhi
 * */
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.SOURCE)
public @interface NotNullable {
}
