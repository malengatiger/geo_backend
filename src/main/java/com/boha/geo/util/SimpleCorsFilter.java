package com.boha.geo.util;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class SimpleCorsFilter implements Filter {
    static final Logger log = LoggerFactory.getLogger(SimpleCorsFilter.class);
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    public SimpleCorsFilter() {
        log.info(xx + " SimpleCorsFilter constructed");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        log.info("\n\n"+E.AMP + E.AMP +E.AMP + E.AMP + " SimpleCorsFilter doFilter ...ServerPort : "
                +E.AMP + E.AMP + req.getServerPort()+ " " + E.AMP + " ServerName: " + req.getServerName());
        Enumeration<String> en = req.getParameterNames();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            log.info(E.AMP +E.AMP + E.AMP + " Param: " + key + " "
                    + E.RED_APPLE + " " + req.getParameter(key));
        }
        log.info(""+E.AMP + E.AMP +E.AMP + E.AMP + " contextPath: " + request.getContextPath()
                + E.AMP +" requestURI: " + request.getRequestURI() + "\n\n");


        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
