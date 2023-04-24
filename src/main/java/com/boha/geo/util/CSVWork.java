package com.boha.geo.util;


import com.boha.geo.monitor.data.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
public class CSVWork {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVWork.class);

//    public static void main(String[] args) {
//        try {
//            buildFiles();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void buildFiles() throws IOException {
        final String userType = "ORG_ADMINISTRATOR";
        List<User> users = new ArrayList<>();

        User u1 = new User();
        u1.setName("John Q Washington");
        u1.setCellphone("+19985550890");
        u1.setEmail("john@email.com");
        u1.setUserType(userType);
        users.add(u1);

        User u2 = new User();
        u2.setName("Nancy Petersen");
        u2.setCellphone("+19985550891");
        u2.setEmail("nancy@email.com");
        u2.setUserType("FIELD_MONITOR");
        users.add(u2);

        User u3 = new User();
        u3.setName("Maria Jones-Mhinga");
        u3.setCellphone("+19985550892");
        u3.setEmail("maria@email.com");
        u3.setUserType("ORG_EXECUTIVE");
        users.add(u3);

        User u4 = new User();
        u4.setName("Thembakazi Tracy Bongo");
        u4.setCellphone("+19985550895");
        u4.setEmail("thembekazi@email.com");
        u4.setUserType("FIELD_MONITOR");
        users.add(u4);

        String json = G.toJson(users);
        LOGGER.info(" \uD83C\uDF00\uD83C\uDF00\uD83C\uDF00\uD83C\uDF00\uD83C\uDF00" + json);
        createCSV(users);
        createJSON(json);


    }

    private static void createJSON(String json) throws IOException {
        File file = new File("peopleA.json");
        Files.writeString(Paths.get(file.toURI()),
                json,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        List<User> list = FileToUsers.getUsersFromJSONFile(file);
        LOGGER.info("Users created from JSON file: \uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E "
                + list.size());
    }

    private static void createCSV(List<User> users) {
        JSONArray docs = new JSONArray();
        LOGGER.info("\n\uD83C\uDF00\uD83C\uDF00 createCSV users: " + users.size());
        try {
            for (User user : users) {
                String json = G.toJson(user);
                JSONObject jsonObject = new JSONObject(json);
                docs.put(jsonObject);
            }

            File file = new File("peopleA.csv");
            String csvString = CDL.toString(docs);
            LOGGER.info("\n\uD83C\uDF00\uD83C\uDF00 csvString: " + csvString);

            FileUtils.writeByteArrayToFile(file, csvString.getBytes());
            List<User> list = FileToUsers.getUsersFromCSVFile(file);
            LOGGER.info("Users created from CSV file: \uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E "
                    + list.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}

