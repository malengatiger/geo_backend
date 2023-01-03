package com.boha.geo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@EnableCaching
@Configuration
public class MongoConfig {
    private static final Logger LOGGER = Logger.getLogger(MongoConfig.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static String mm = E.AMP + E.AMP + E.AMP;

    @Value("${spring.data.mongodb.uri.one}")
    private String mongoPrefix;

    @Value("${mongoString}")
    private String mongoString;

    @Value("${spring.data.mongodb.uri.two}")
    private String mongoSuffix;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    public MongoClient mongo() {
        String uri;
        if (mongoString != null) {
            LOGGER.info(E.RAIN+E.RAIN+E.RAIN+E.RAIN+
                    " Using local MongoDB Server with " + mongoString);
            uri = mongoString;
        } else {
            String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
            uri = mongoPrefix + encodedPassword + mongoSuffix;
        }
        LOGGER.info(mm + "MongoDB Connection string: " + E.RED_APPLE + " with encoded password: " + uri);
//        String enc = URLEncoder.encode(mongoString, StandardCharsets.UTF_8);
//        LOGGER.info(mm + "MongoDB Connection string: " + E.RED_APPLE + "encoded:" + mongoString);

        ConnectionString connectionString = new ConnectionString(uri);
        LOGGER.info(mm + "MongoDB Connection userName: " + E.RED_APPLE + " = " + connectionString.getUsername());

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        LOGGER.info(mm + "MongoClientSettings have been set with pojoCodecRegistry");
        MongoClient client = MongoClients.create(settings);
        LOGGER.info(mm + " " + client.listDatabases().iterator().getServerAddress() + " MongoClientSettings have been set with pojoCodecRegistry");
        for (Document document : client.listDatabases()) {
            LOGGER.info(mm + "Database Document: " + document.toJson() + E.RAIN);
        }
        LOGGER.info(mm + " ClusterDescription: "
                + client.getClusterDescription().getShortDescription() + mm);
        LOGGER.info(mm + " Database Name: "
                + client.getDatabase("geo").getName() + " " + mm);


        return client;


    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate t = new MongoTemplate(mongo(), "geo");
        LOGGER.info(mm + " Geo DB Collections " + mm);
        for (String collectionName : t.getCollectionNames()) {
            LOGGER.info(mm + " Collection: "
                    + collectionName + " " + E.BLUE_DOT);
            MongoCollection<org.bson.Document> col = t.getCollection(collectionName);

            LOGGER.info(mm + " Number of Documents: "
                    + col.countDocuments() + " " + E.BLUE_DOT);
            ListIndexesIterable<org.bson.Document> iter = col.listIndexes();
            for (org.bson.Document doc : iter) {
                LOGGER.info(bb + "index: "
                        + doc.toJson() + "" + E.BLUE_DOT);
            }
        }

        return t;
    }
    private static final String bb = E.YELLOW_STAR ;
}
