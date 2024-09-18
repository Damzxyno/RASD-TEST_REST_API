package com.damzxyno.salesportaltest.cutom_auth_report;

import com.damzxyno.rasdspringapi.core.interfaces.ProvisionalClassDocProviderAdapter;
import com.damzxyno.rasdspringapi.core.interfaces.SecurityMapperProxy;
import com.damzxyno.rasdspringapi.models.TimeRange;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.damzxyno.salesportaltest.config.TimeRestricted;

import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;

@Component
public class TimeBasedAccessControlDocProvider implements ProvisionalClassDocProviderAdapter {
    @Override
    public void configure(RequestMappingHandlerMapping requestMappingHandlerMapping, SecurityMapperProxy securityMapperProxy) {
        requestMappingHandlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethodInfo) -> {
            Class<?> controllerClass = handlerMethodInfo.getBeanType();
            Method handlerMethod = handlerMethodInfo.getMethod();
            if (controllerClass.isAnnotationPresent(TimeRestricted.class)) {
                TimeRestricted timeRestricted = controllerClass.getAnnotation(TimeRestricted.class);
                processTimeRestricted(securityMapperProxy, mappingInfo, timeRestricted);
            }
            if(handlerMethod.isAnnotationPresent(TimeRestricted.class)){
                TimeRestricted timeRestricted = handlerMethod.getAnnotation(TimeRestricted.class);
                processTimeRestricted(securityMapperProxy, mappingInfo, timeRestricted);
            }
        });
    }

    private void processTimeRestricted(SecurityMapperProxy securityMapperProxy, RequestMappingInfo mappingInfo, TimeRestricted timeRestricted) {
        EnumMap<DayOfWeek, TimeRange> acceptedTimeRangeEnum = getTimeRange(timeRestricted, true);
        EnumMap<DayOfWeek, TimeRange> rejectedTimeRangeEnum = getTimeRange(timeRestricted, false);

        mappingInfo.getMethodsCondition().getMethods().forEach(method -> {
            String[] patternValues = mappingInfo.getPatternValues().toArray(new String[0]);
            securityMapperProxy.addTimeAccepted(patternValues, method, acceptedTimeRangeEnum);
            securityMapperProxy.addTimeRestricted(patternValues, method, rejectedTimeRangeEnum);
        });
    }

    private EnumMap<DayOfWeek, TimeRange> getTimeRange(TimeRestricted timeRestricted, boolean isAcceptedRange) {
        LocalTime start = LocalTime.parse(timeRestricted.startTime());
        LocalTime end = LocalTime.parse(timeRestricted.endTime());
        TimeRange timeRange = new TimeRange(start, end);
        EnumMap<DayOfWeek, TimeRange> map = new EnumMap<>(DayOfWeek.class);

        // Populate accepted or rejected days based on the boolean flag
        if (isAcceptedRange) {
            Arrays.stream(timeRestricted.acceptedDays()).forEach(day -> map.put(day, timeRange));

            if (timeRestricted.restrictedDays().length != 0) {
                EnumSet.allOf(DayOfWeek.class).forEach(day -> map.put(day, timeRange));
                Arrays.stream(timeRestricted.restrictedDays()).forEach(map::remove);
            }
        } else {
            Arrays.stream(timeRestricted.restrictedDays()).forEach(day -> map.put(day, timeRange));
        }

        return map;
    }
}
