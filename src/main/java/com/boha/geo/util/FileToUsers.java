package com.boha.geo.util;

import com.boha.geo.monitor.data.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.CDL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileToUsers {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(FileToUsers.class);

    public static List<User> getUsersFromJSONFile(File file) throws IOException {
        List<User> users = new ArrayList<>();

        try {
            Path filePath = Path.of(file.getPath());
            String json = Files.readString(filePath);
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            users = G.fromJson(json, listType);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static List<User> getUsersFromCSVFile(File file) throws IOException {
        List<User> users = new ArrayList<>();
        try (FileInputStream is = new FileInputStream(file)) {
            String csv = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try {
                String json = CDL.toJSONArray(csv).toString(2);
                Type listType = new TypeToken<ArrayList<User>>() {
                }.getType();
                users = G.fromJson(json, listType);

                Files.write(Path.of("people2.json"), json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E User objects created: " + users.size());
        return users;
    }
}
