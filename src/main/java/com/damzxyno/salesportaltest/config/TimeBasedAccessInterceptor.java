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
public class TimeBasedAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            TimeRestricted timeRestricted = method.getMethodAnnotation(TimeRestricted.class);

            if (timeRestricted != null) {
                LocalTime now = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                LocalTime startTime = LocalTime.parse(timeRestricted.startTime(), formatter);
                LocalTime endTime = LocalTime.parse(timeRestricted.endTime(), formatter);

                if (now.isBefore(startTime) || now.isAfter(endTime)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access not allowed at this time");
                    return false;
                }

                Set<DayOfWeek> restrictedDays = Arrays.stream(timeRestricted.restrictedDays()).collect(Collectors.toSet());
                Set<DayOfWeek> acceptedDays = Arrays.stream(timeRestricted.acceptedDays()).collect(Collectors.toSet());
                if (!restrictedDays.isEmpty() && restrictedDays.contains(LocalDate.now().getDayOfWeek())){
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access not allowed today");
                    return false;
                }
                if (!acceptedDays.isEmpty() && !acceptedDays.contains(LocalDate.now().getDayOfWeek())){
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access not allowed today");
                    return false;
                }
            }
        }
        return true;
    }
}
