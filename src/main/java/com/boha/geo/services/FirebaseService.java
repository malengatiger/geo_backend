package com.boha.geo.services;

import com.boha.geo.util.E;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Initializes Firebase
 */
@Service
public class FirebaseService {
    private static final Logger LOGGER = Logger.getLogger(FirebaseService.class.getSimpleName());

    public FirebaseService() {
        LOGGER.info(E.AMP+E.AMP+E.AMP + " FirebaseService constructed");
    }
//    @Autowired
//    private Environment environment;
    private FirebaseApp app;
    public FirebaseApp initializeFirebase() {
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
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Firebase initialization failed!  " + e.getMessage());
        }

        app = FirebaseApp.initializeApp(options);
        LOGGER.info(E.AMP+E.AMP+E.AMP+E.AMP+E.AMP+
                " Firebase has been initialized: "
                + app.getOptions().getDatabaseUrl()
                + " " + E.RED_APPLE);
        return app;
    }
}
