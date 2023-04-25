package com.boha.geo.config;

import com.boha.geo.services.SecretManagerService;
import com.boha.geo.util.E;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.slf4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;


import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
@RequiredArgsConstructor
@EnableCaching
@Configuration
public class MongoConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(MongoConfig.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static String mm = E.AMP + E.AMP + E.AMP;

    final SecretManagerService secretManagerService;

    @Value("${mongoString}")
    private String mongoString;

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongo() {
        LOGGER.info(E.RAIN+E.RAIN+E.RAIN+E.RAIN+
                " MongoClient bean prep, active profile: " + profile);

        String mString = "";

        String uri;
//        if (profile.equalsIgnoreCase("dev")) {
//            LOGGER.info(E.RAIN+E.RAIN+E.RAIN+E.RAIN+
//                    " Using local MongoDB Server with " + mongoString);
//            uri = mongoString;
//        } else {
            try {

                uri = secretManagerService.getMongoString();
                int index = uri.indexOf("@");
                if (index > -1) {
                    mString = uri.substring(index);
                }
                LOGGER.info(E.RAIN+E.RAIN+E.RAIN+E.RAIN+
                        " Using MongoDB Atlas Server with " + E.BLUE_DOT + " " + mString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//        }


        LOGGER.info(mm + "MongoDB Connection string: " + E.RED_APPLE + mString);

        ConnectionString connectionString = new ConnectionString(uri);
        LOGGER.info(mm + "MongoDB Connection userName: " + E.RED_APPLE + " : "
                + connectionString.getUsername());

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        LOGGER.info(mm + "MongoClientSettings have been set with pojoCodecRegistry");

        MongoClient client = MongoClients.create(settings);

        LOGGER.info(mm + " " + client.listDatabases().iterator().getServerAddress() + " MongoClientSettings have been set with pojoCodecRegistry");
//        for (Document document : client.listDatabases()) {
//            LOGGER.info(mm + "Database Document: " + document.toJson() + E.RAIN);
//        }
//        LOGGER.info(mm + " ClusterDescription: "
//                + client.getClusterDescription().getShortDescription() + mm);
        LOGGER.info(mm + " Database Name: "
                + client.getDatabase(databaseName).getName() + " " + mm);


        return client;


    }

    private final SecretManagerTemplate secretManagerTemplate;
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate t = new MongoTemplate(mongo(), databaseName);
//        LOGGER.info(mm + " Geo DB Collections " + mm);
//        List<String> cols = new ArrayList<>(t.getCollectionNames());
//        Collections.sort(cols);
//        for (String col : cols) {
//            LOGGER.info(mm + " Collection: "
//                    + col + " " + E.BLUE_DOT);
//        }

        return t;
    }
    private static final String bb = E.YELLOW_STAR ;
    public String getSecret(
             String secretId,
            String version,
            String projectId,
            ModelMap map) {

        if (StringUtils.isEmpty(version)) {
            version = SecretManagerTemplate.LATEST_VERSION;
        }

        String secretPayload;
        if (StringUtils.isEmpty(projectId)) {
            secretPayload = this.secretManagerTemplate.getSecretString(
                    "sm://" + secretId + "/" + version);
        }
        else {
            secretPayload = this.secretManagerTemplate.getSecretString(
                    "sm://" + projectId + "/" + secretId + "/" + version);
        }

        return secretPayload;
    }
}
