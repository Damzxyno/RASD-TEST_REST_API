package com.damzxyno.salesportaltest.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.DayOfWeek;
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeRestricted {
    String startTime();
    String endTime();
    String timeZone();
    DayOfWeek [] acceptedDays() default {};
    DayOfWeek [] restrictedDays() default {};
}

