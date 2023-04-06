package com.boha.geo.services;

import com.boha.geo.monitor.data.Organization;
import com.boha.geo.monitor.data.User;
import com.boha.geo.monitor.services.DataService;
import com.boha.geo.repos.OrganizationRepository;
import com.boha.geo.util.FileToUsers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserBatchService {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBatchService.class);

    final DataService dataService;
    final OrganizationRepository organizationRepository;

    public UserBatchService(DataService dataService, OrganizationRepository organizationRepository) {
        this.dataService = dataService;
        this.organizationRepository = organizationRepository;
    }

    public  List<User> handleUsersFromJSON(File file, String organizationId, String translatedTitle, String translatedMessage) throws Exception {
        Organization org = organizationRepository.findByOrganizationId(organizationId);
        List<User> users = FileToUsers.getUsersFromJSONFile(file);
        return processUsers(organizationId, org, users, translatedTitle, translatedMessage);
    }

    private  List<User> processUsers(String organizationId, Organization org, List<User> users, String translatedTitle, String translatedMessage) throws Exception {
        List<User> usersCreated = new ArrayList<>();
        LOGGER.info("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 processUsers: " + users.size() + " users");
        for (User user : users) {
            user.setOrganizationId(organizationId);
            user.setOrganizationName(org.getName());
            user.setPassword(UUID.randomUUID().toString());
            user.setTranslatedTitle(translatedTitle);
            user.setTranslatedMessage(translatedMessage);
            user.setCreated(DateTime.now().toDateTimeISO().toString());
            dataService.createUser(user);
            usersCreated.add(user);
            LOGGER.info("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 processUsers: user created " + user.getName());
        }
        LOGGER.info("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 processUsers: users created " + usersCreated.size() + " users");
        return usersCreated;
    }

    public  List<User> handleUsersFromCSV(File file,String organizationId,String translatedTitle, String translatedMessage) throws Exception {
        Organization org = organizationRepository.findByOrganizationId(organizationId);
        List<User> users = FileToUsers.getUsersFromCSVFile(file);
        return processUsers(organizationId, org, users,translatedTitle,translatedMessage);
    }
}
