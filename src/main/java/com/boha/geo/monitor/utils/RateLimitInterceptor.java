package com.boha.geo.monitor.utils;

import com.boha.geo.util.E;
import com.google.firebase.database.annotations.NotNull;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
//@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitInterceptor.class.getSimpleName());
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    private final Bucket bucket;

    private final int numTokens;
    private static final String mm = E.BLUE_DOT + E.BLUE_DOT + E.BLUE_DOT + E.BLUE_DOT;

    public RateLimitInterceptor(Bucket bucket, int numTokens) {
        this.bucket = bucket;
        this.numTokens = numTokens;
        LOGGER.info(xx + "RateLimitInterceptor construction, tokens: " + numTokens + " bucket tokens: " + bucket.getAvailableTokens());
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                             @NotNull Object handler) {

        ConsumptionProbe probe = this.bucket.tryConsumeAndReturnRemaining(this.numTokens);
        if (probe.isConsumed()) {
            LOGGER.info(mm + E.OK + E.OK + " RateLimitInterceptor: RemainingTokens: "
                    + probe.getRemainingTokens() + " " + E.YELLOW_BIRD + " Milliseconds to wait: "
                    + TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill()) + E.YELLOW_BIRD );
            response.addHeader("X-Rate-Limit-Remaining",
                    Long.toString(probe.getRemainingTokens()));
            return true;
        }

        LOGGER.info(mm + E.NOT_OK + E.NOT_OK +  E.NOT_OK
                +  "RateLimitInterceptor: RemainingTokens: "
                + probe.getRemainingTokens() + " " + E.ERROR);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        try {
            response.getWriter().append("Too many requests");
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.addHeader("X-Rate-Limit-Retry-After-Milliseconds",
                Long.toString(TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill())));

        return false;

    }
}
