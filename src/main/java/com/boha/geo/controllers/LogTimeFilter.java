package com.boha.geo.controllers;

import com.boha.geo.util.E;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogTimeFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogTimeFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        try {
            HttpServletRequest req = (HttpServletRequest)request;
            String url = req.getRequestURI();
            chain.doFilter(request, response);
            long end = System.currentTimeMillis();
            long ms = end - startTime;
            double dur = Double.parseDouble("" + ms) / Double.parseDouble("1000");
            LOGGER.info(E.DICE+E.DICE +E.DICE+E.DICE + url + ": Elapsed "
                    +E.RED_APPLE+ " " + dur + " seconds");
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }

    }
}
