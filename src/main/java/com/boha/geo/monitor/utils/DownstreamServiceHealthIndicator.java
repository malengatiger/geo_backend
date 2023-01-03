package com.boha.geo.monitor.utils;

import com.boha.geo.util.E;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class DownstreamServiceHealthIndicator implements ReactiveHealthIndicator {
    private static final Logger LOGGER = Logger.getLogger(DownstreamServiceHealthIndicator.class.getSimpleName());

    public DownstreamServiceHealthIndicator() {
        LOGGER.info(E.PRESCRIPTION.concat(E.PRESCRIPTION.concat(" DownstreamServiceHealthIndicator will observe "
        .concat(E.PRESCRIPTION))));
    }

    @Override
    public Mono<Health> health() {
        return checkDownstreamServiceHealth().onErrorResume(
                ex -> Mono.just(new Health.Builder().down(ex).build())
        );
    }


    private Mono<Health> checkDownstreamServiceHealth() {
        // we could use WebClient to check health reactively
        return Mono.just(new Health.Builder().up().build());
    }
}
