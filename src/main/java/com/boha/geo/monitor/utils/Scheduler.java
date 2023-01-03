package com.boha.geo.monitor.utils;

import com.boha.geo.monitor.services.DataService;
import com.boha.geo.util.E;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Scheduler {
    public static final Logger LOGGER = Logger.getLogger(Scheduler.class.getSimpleName());
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private DataService dataService;


    public Scheduler() {
        LOGGER.info(xx +
                " Scheduler constructed. Waiting to be triggered ");
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 4) //4 Hours

    public void fixedRateScheduled() throws Exception {
        LOGGER.info(E.PRETZEL.concat(E.PRETZEL).concat(E.PRETZEL) + "Fixed Rate scheduler; " +
                "\uD83C\uDF3C CALCULATE LOAN BALANCES or OTHER NECESSARY WORK: " + new DateTime().toDateTimeISO().toString()
                + " " + E.RED_APPLE);

        try {

            LOGGER.info(E.PRETZEL.concat(E.PRETZEL).concat(E.PRETZEL) +
                    "Nothing do do thus far ... ");

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(E.NOT_OK.concat(E.NOT_OK) + "Firebase fell down");
//            e.printStackTrace();
        }
    }


}
