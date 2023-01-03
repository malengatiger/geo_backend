package com.boha.geo.controllers;

import com.boha.geo.monitor.utils.MongoGenerator;
import com.boha.geo.util.E;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController

public class GeneratorController {
    private static final Logger LOGGER = Logger.getLogger(GeneratorController.class.getSimpleName());
    private final MongoGenerator mongoGenerator;

    public GeneratorController(MongoGenerator mongoGenerator) {
        this.mongoGenerator = mongoGenerator;
    }

    @GetMapping("/generate")
    public ResponseEntity<Object> generate() throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("generate: Generate Monitor Demo users and data ...."));
        try {
            mongoGenerator.generate(4);
            String msg = E.RAIN_DROPS + E.RAIN_DROPS + " " +
                    "..... MongoGenerator: Monitor Demo users and data added OK! Mission completed!!"
                    + E.RED_APPLE;
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Generation failed");
        }
    }
}
