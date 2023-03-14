package com.boha.geo.monitor.utils;

import com.boha.geo.monitor.services.DataService;
import com.boha.geo.util.E;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.annotations.NotNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class MonitorAuthenticationFilter extends OncePerRequestFilter {
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;
    String mm = "" + E.AMP + E.AMP + E.AMP + E.AMP;

    public MonitorAuthenticationFilter() {
        LOGGER.info(xx +
                "MonitorAuthenticationFilter : constructor \uD83D\uDE21");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorAuthenticationFilter.class);

    @Autowired
    private DataService dataService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest,
                                    @NotNull HttpServletResponse httpServletResponse,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURL().toString();
        //LOGGER.info(mm+ " url: " + url);
        //        if (url.contains("192.168.86.230:8080") || url.contains("172.20.10.4:8080")|| url.contains("localhost:8080")) {   //this is my local machine
        if (url.contains("localhost:") || url.contains("uploadFile")) {   //this is my local machine
            LOGGER.info(E.ANGRY + E.ANGRY + "this request is not subject to authentication: "
                    + E.HAND2 + url);
            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        //allow getCountries
        if (httpServletRequest.getRequestURI().contains("getCountries")
                || httpServletRequest.getRequestURI().contains("addCountry")) {
            LOGGER.info("" + mm + " contextPath: " + httpServletRequest.getContextPath()
                    + E.AMP + " requestURI: " + httpServletRequest.getRequestURI());
            LOGGER.info("" + mm + " allowing addCountry and getCountries without authentication, is this OK?");

            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        //allow api-docs
        if (httpServletRequest.getRequestURI().contains("api-docs")) {
            LOGGER.info("" + mm + " contextPath: " + httpServletRequest.getContextPath()
                    + E.AMP + " requestURI: " + httpServletRequest.getRequestURI() + "\n\n");
            LOGGER.info("" + mm + " allowing swagger openapi call");

            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }

        String m = httpServletRequest.getHeader("Authorization");
        if (m == null) {
            httpServletResponse.sendError(403, "GTFO");
            return;
        }
        String token = m.substring(7);
        try {
            //dataService.initializeFirebase();
            ApiFuture<FirebaseToken> future = FirebaseAuth.getInstance().verifyIdTokenAsync(token, true);
            FirebaseToken mToken = future.get();
            if (mToken != null) {
                doFilter(httpServletRequest, httpServletResponse, filterChain);
            } else {
                httpServletResponse.sendError(403, "GTFO");
            }

        } catch (Exception e) {
            String msg = "\uD83D\uDD06 \uD83D\uDD06 \uD83D\uDD06 " +
                    "FirebaseAuthException happened: \uD83C\uDF4E " + e.getMessage();
            LOGGER.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 " + msg);
            httpServletResponse.sendError(403, "GTFO");
        }

    }

    private void doFilter(@NotNull HttpServletRequest httpServletRequest,
                          @NotNull HttpServletResponse httpServletResponse,
                          FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        String url = httpServletRequest.getRequestURL().toString();
        LOGGER.info("\uD83D\uDD37\uD83D\uDD37\uD83D\uDD37 Status Code: "
                + httpServletResponse.getStatus() + "  \uD83D\uDD37 "
                + httpServletRequest.getRequestURI() + "  \uD83D\uDD37 \uD83D\uDD37 \uD83D\uDD37 ");
    }

    private void print(@NotNull HttpServletRequest httpServletRequest) {
        String url = httpServletRequest.getRequestURL().toString();
        LOGGER.info(E.ANGRY + E.ANGRY + E.ANGRY + E.BELL + "Authenticating this url: " + E.BELL + " " + url);

        System.out.println("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 request header parameters ...");
        Enumeration<String> parms = httpServletRequest.getParameterNames();
        while (parms.hasMoreElements()) {
            String m = parms.nextElement();
            LOGGER.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 parameterName: " + m);

        }
        LOGGER.info("\uD83D\uDE21 \uD83D\uDE21 headers ...");
        Enumeration<String> names = httpServletRequest.getHeaderNames();
        while (names.hasMoreElements()) {
            String m = names.nextElement();
            LOGGER.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 headerName: " + m);
        }
        LOGGER.info("\uD83D\uDC9A \uD83D\uDC9A \uD83D\uDC9A Authorization: "
                + httpServletRequest.getHeader("Authorization") + " \uD83D\uDC9A \uD83D\uDC9A");
    }

}

