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

//        print(httpServletRequest);

        String url = httpServletRequest.getRequestURL().toString();
        LOGGER.info(E.RED_APPLE+E.RED_APPLE+ " url: " + url);
        //        if (url.contains("192.168.86.230:8080") || url.contains("172.20.10.4:8080")|| url.contains("localhost:8080")) {   //this is my local machine
        if (url.contains("localhost:8080")) {   //this is my local machine
            LOGGER.info(E.ANGRY + "this request is not subject to authentication: "
                    + E.HAND2 + url);
            String m = httpServletRequest.getHeader("Authorization");
            String s;
            if (m == null) {
                s = E.RED_DOT + E.RED_DOT + E.RED_DOT + E.RED_DOT
                        + " ... but you do NOT have an auth key anyway!";
            } else {
                s = E.AMP + E.AMP + E.AMP + E.AMP
                        + " ... but you do have an auth key. " + E.RED_APPLE + " Cool!!";
            }
            LOGGER.info(s);
            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        //allow getCountries
        if (httpServletRequest.getRequestURI().contains("getCountries")) {
            LOGGER.info("" + E.AMP + E.AMP + E.AMP + E.AMP + " contextPath: " + httpServletRequest.getContextPath()
                    + E.AMP + " requestURI: " + httpServletRequest.getRequestURI() + "\n\n");

            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        LOGGER.info(E.ANGRY + E.ANGRY + "this request IS subject to authentication: "
                + E.HAND2 + url);
        String m = httpServletRequest.getHeader("Authorization");
        if (m == null) {
            String msg = "\uD83D\uDC7F \uD83D\uDC7F \uD83D\uDC7F " +
                    "Authorization Header is missing. Needs JWT token! \uD83C\uDF4E "
                    + httpServletRequest.getQueryString() + " \uD83C\uDF4E \uD83C\uDF4E";
            LOGGER.info(msg);
            httpServletResponse.sendError(403, "GTFO");
            return;
//            throw new ServletException("Forbidden!");
        }
        String token = m.substring(7);
        try {
            dataService.initializeFirebase();
            ApiFuture<FirebaseToken> future = FirebaseAuth.getInstance().verifyIdTokenAsync(token, true);
            FirebaseToken mToken = future.get();
            LOGGER.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 Authentication executed, uid: "
                    + mToken.getUid() + " \uD83D\uDE21 email: " + mToken.getEmail()
                    + "  \uD83C\uDF38" +
                    " \uD83C\uDF4E request authenticated OK!! \uD83C\uDF4E");
            doFilter(httpServletRequest, httpServletResponse, filterChain);

        } catch (Exception e) {
            String msg = "\uD83D\uDD06 \uD83D\uDD06 \uD83D\uDD06 " +
                    "FirebaseAuthException happened: \uD83C\uDF4E " + e.getMessage();
            LOGGER.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 " + msg);
            httpServletResponse.sendError(403, "GTFO");
//            throw new ServletException("Forbidden!");
        }

    }

    private void doFilter(@NotNull HttpServletRequest httpServletRequest,
                          @NotNull HttpServletResponse httpServletResponse,
                          FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        String url = httpServletRequest.getRequestURL().toString();
        LOGGER.info("\uD83D\uDD37 \uD83D\uDD37 \uD83D\uDD37 Response Status Code: "
                + httpServletResponse.getStatus() + "  \uD83D\uDD37 \uD83D\uDD37 \uD83D\uDD37 " + url + "  \uD83D\uDD37 \uD83D\uDD37 \uD83D\uDD37 ");
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

