package com.damzxyno.salesportaltest.cutom_auth_report;

import com.damzxyno.rasdspringapi.core.interfaces.ProvisionalClassDocProviderAdapter;
import com.damzxyno.rasdspringapi.core.interfaces.SecurityMapperProxy;
import com.damzxyno.salesportaltest.config.LocationRestricted;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationBasedAccessControlDocProvider implements ProvisionalClassDocProviderAdapter {
    @Override
    public void configure(RequestMappingHandlerMapping requestMappingHandlerMapping, SecurityMapperProxy securityMapperProxy) {
        requestMappingHandlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethodInfo) -> {
            Class<?> controllerClass = handlerMethodInfo.getBeanType();
            Method handlerMethod = handlerMethodInfo.getMethod();
            if (controllerClass.isAnnotationPresent(LocationRestricted.class)) {
                LocationRestricted locationRestricted = controllerClass.getAnnotation(LocationRestricted.class);
                processLocationAccepted(securityMapperProxy, mappingInfo, locationRestricted);
            }
            if(handlerMethod.isAnnotationPresent(LocationRestricted.class)){
                LocationRestricted locationRestricted = handlerMethod.getAnnotation(LocationRestricted.class);
                processLocationAccepted(securityMapperProxy, mappingInfo, locationRestricted);
            }
        });
    }

    private void processLocationAccepted(SecurityMapperProxy securityMapperProxy, RequestMappingInfo mappingInfo, LocationRestricted locationRestricted) {
        List<String> acceptedLocations = Arrays.stream(locationRestricted.locationAccepted()).collect(Collectors.toList());

        mappingInfo.getMethodsCondition().getMethods().forEach(method -> {
            String[] patternValues = mappingInfo.getPatternValues().toArray(new String[0]);
            securityMapperProxy.addLocationAccepted(patternValues, method, acceptedLocations);
        });
    }
}
