package com.boha.geo.monitor.utils;

import com.boha.geo.util.E;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class EndpointsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointsListener.class);
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    public EndpointsListener() {
        LOGGER.info(xx +" EndpointsListener: Listening to Endpoints ... ");
    }
    int cnt = 0;
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        List<String> sorted = new ArrayList<>();
        applicationContext.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods().forEach((key, value) -> {
                    cnt++;
                    sorted.add(String.valueOf(key));
        });
        LOGGER.info(E.DIAMOND.concat(E.DIAMOND).concat("Total Number of Endpoints: " + cnt));
        Collections.sort(sorted);
        for (String s : sorted) {
            LOGGER.info("Endpoint: " + E.RED_APPLE+E.RED_APPLE + "{}", s);
        }
        LOGGER.info(E.DIAMOND.concat(E.DIAMOND).concat("Total Number of Endpoints: " + sorted.size()));

    }
}
