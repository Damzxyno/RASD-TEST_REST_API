package com.damzxyno.salesportaltest.config;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LocationBasedAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            LocationRestricted locationRestricted = method.getMethodAnnotation(LocationRestricted.class);

            if (locationRestricted != null) {
                String[] locationAccepted = locationRestricted.locationAccepted();
                String userLocation = request.getHeader("X-User-Location");

                if (userLocation == null || !Arrays.asList(locationAccepted).contains(userLocation)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }

            }
        }
        return true;
    }
}
