package com.boha.geo.util;

import com.boha.geo.monitor.data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JsonGen {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LoggerFactory.getLogger(JsonGen.class);

    private final MongoTemplate mongoTemplate;

    public JsonGen(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void startTestDataGeneration() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        Path dirPath = Files.createDirectory(Path.of("testData_" + sdf.format(new Date())));
        createJsonFiles(dirPath);
    }

    private void createJsonFiles(Path dirPath) throws IOException {
        String organizationId = "organizationId";
        String id = "8b6af025-1821-4f29-995a-77c80e9b0996";

        Criteria c = Criteria.where(organizationId)
                .is(id);
        Query query = new Query(c).limit(20);
        List<Photo> photos = mongoTemplate.find(query, Photo.class);
        String json4 = G.toJson(photos);
        createFile(dirPath, json4,"photos.json");

        Query query2 = new Query(c).limit(20);
        List<Video> videos = mongoTemplate.find(query2, Video.class);
        String json5 = G.toJson(videos);
        createFile(dirPath, json5,"videos.json");

        Query query3 = new Query(c).limit(20);
        List<Audio> audios = mongoTemplate.find(query3, Audio.class);
        String json6 = G.toJson(audios);
        createFile(dirPath, json6,"audios.json");


        Query query4 = new Query(c).limit(20);
        List<ProjectPosition> positions = mongoTemplate.find(query4, ProjectPosition.class);
        String json7 = G.toJson(positions);
        createFile(dirPath, json7,"projectPositions.json");

        Query query5 = new Query(c).limit(20);
        List<ProjectPolygon> projectPolygons = mongoTemplate.find(query5, ProjectPolygon.class);
        String json8 = G.toJson(projectPolygons);
        createFile(dirPath, json8,"projectPolygons.json");

        Query query6 = new Query(c).limit(20);
        List<User> mUsers = mongoTemplate.find(query6, User.class);
        String json9 = G.toJson(mUsers);
        createFile(dirPath, json9,"users.json");

        List<Country> countryList = mongoTemplate.findAll(Country.class);
        String json10 = G.toJson(countryList);
        createFile(dirPath, json10,"countries.json");

        List<Organization> organizations = mongoTemplate.findAll(Organization.class);
        String json11 = G.toJson(organizations);
        createFile(dirPath, json11,"organizations.json");


    }

    private void createFile(Path dirPath, String json, String name) throws IOException {
        try {
            Path path = Path.of(dirPath + "/" + name);
            Files.writeString(path, json,
                    StandardCharsets.UTF_8);
            logger.info("\uD83D\uDD35 \uD83D\uDD35 \uD83D\uDD35 "+name+" output path: " + path.toFile().getAbsolutePath());
        } catch (Exception e) {
            logger.info("\uD83D\uDD34\uD83D\uDD34 \uD83D\uDD34\uD83D\uDD34 unable to write file: " + e.getMessage());
        }
    }

}
