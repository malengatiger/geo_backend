package com.boha.geo.services;

import com.boha.geo.monitor.services.DataService;
import com.boha.geo.util.E;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Initializes Firebase
 */
@RequiredArgsConstructor
@Service
public class FirebaseService {
    final DataService dataService;

    private static final Logger LOGGER = Logger.getLogger(FirebaseService.class.getSimpleName());

    @Value("${storageBucket}")
    private String storageBucket;
    private FirebaseApp app;



    public void initializeFirebase() {
        LOGGER.info(E.AMP+E.AMP+E.AMP+ " .... initializing Firebase ....");
        FirebaseOptions options;
        String projectId = System.getenv().get("PROJECT_ID");
        if (projectId == null) {
            LOGGER.info(E.RED_DOT+E.RED_DOT+E.AMP+ " .... missing ProjectId WTF? ....");
            throw  new RuntimeException("Project  ID is missing from environment variables");
        }
        LOGGER.info(E.AMP+E.AMP+E.AMP+
                " Project Id from System.getenv: "+E.RED_APPLE + " " + projectId);
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://" + projectId + ".firebaseio.com/")
                    .setStorageBucket(storageBucket)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Firebase initialization failed!  " + e.getMessage());
        }

        app = FirebaseApp.initializeApp(options);
        LOGGER.info(E.AMP+E.AMP+E.AMP+E.AMP+E.AMP+
                " Firebase has been initialized: "
                + app.getOptions().getDatabaseUrl()
                + " " + E.RED_APPLE);
    }

}
