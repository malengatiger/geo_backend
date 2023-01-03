package com.boha.geo.services;

import com.boha.geo.models.User;
import com.boha.geo.monitor.data.City;
import com.boha.geo.repos.CityRepo;
import com.boha.geo.repos.UserRepo;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class UserService {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = Logger.getLogger(UserService.class.getSimpleName());
    private static final String alien = E.PEAR+E.PEAR+E.PEAR;
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    final MongoService mongoService;
    final CityRepo cityRepo;
    final StorageService storageService;
    final UserRepo userRepo;


    public UserService(MongoService mongoService, CityRepo cityRepo,
                       StorageService storageService,
                       UserRepo userRepo) {
        this.mongoService = mongoService;
        this.cityRepo = cityRepo;
        this.storageService = storageService;
        this.userRepo = userRepo;

        logger.info(xx
                + " UserService constructed and services injected");
    }
    @Value("${surnames}")
    private String surnamesFile;
    @Value("${firstNames}")
    private String firstNamesFile;
    private final List<String> firstNames = new ArrayList<>();
    private final List<String> lastNames = new ArrayList<>();
    private final List<String> middleInitials = new ArrayList<>();

    private void setLastNames() {
        String mSurnames = storageService.downloadObject(surnamesFile);
        JSONArray arr = new JSONArray(mSurnames);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String surname = obj.getString("surname");
            String real = processName(surname);
            lastNames.add(real);
        }
        logger.info(E.LEAF + E.LEAF +
                " Number of LastNames: " + lastNames.size());
    }

    private String processName(String name) {
        String first = name.substring(0, 1);
        String capFirst = first.toUpperCase();
        String rest = name.substring(1);
        String lowRest = rest.toLowerCase();
        return capFirst + lowRest;
    }

    private void setFirstNames() {
        String mFirstNames = storageService.downloadObject(firstNamesFile);
        JSONArray arr = new JSONArray(mFirstNames);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String firstName = obj.getString("name");
            String real = processName(firstName);
            firstNames.add(real);
        }
        logger.info(E.LEAF + E.LEAF +
                " Number of FirstNames: " + firstNames.size());


    }

    private void setMiddleInitials() {
        middleInitials.add("A");
        middleInitials.add("B");
        middleInitials.add("C");
        middleInitials.add("D");
        middleInitials.add("E");
        middleInitials.add("F");
        middleInitials.add("G");
        middleInitials.add("H");
        middleInitials.add("J");
        middleInitials.add("K");
        middleInitials.add("L");
        middleInitials.add("M");
        middleInitials.add("N");
        middleInitials.add("O");
        middleInitials.add("P");
        middleInitials.add("Q");
        middleInitials.add("R");
        middleInitials.add("S");
        middleInitials.add("T");
        middleInitials.add("X");
        middleInitials.add("Z");

    }

    List<City> cityList = new ArrayList<>();
    int userTotal = 0;
    Random random = new Random(System.currentTimeMillis());
    int cityCounter = 0;

    public int addUsers() throws Exception {
        long start = System.currentTimeMillis();
        if (cityList == null || cityList.isEmpty()) {
            cityList = cityRepo.findAll();
            Collections.sort(cityList);
        }
        userTotal = 0;
        int maxPerCityCount = 1000;
        if (lastNames.size() == 0) {
            setLastNames();
            setFirstNames();
            setMiddleInitials();
        }

        setSpecialCities();

        for (City city : cityList) {
            int count = random.nextInt(maxPerCityCount);
            if (count < 100) count = 200;
            if (specialCities.containsKey(city.getName())) {
                int m = random.nextInt(10000);
                if (m < 5000) m = 5000;
                count += m;
                userTotal += processCityUsers(count, city);
            } else {
                int ignore = random.nextInt(1000);
                if (ignore > 800) {
                    logger.info(E.YELLOW + E.YELLOW +
                            " City ignored, no users " + city.getName());
                } else {
                    userTotal += processCityUsers(count, city);

                }
            }
        }

        long end = System.currentTimeMillis();
        long elapsed = (end-start)/1000;
        logger.info(E.LEAF + E.LEAF + E.LEAF +
                " Users generated for " + cityList.size() + " cities:  "
                + E.RED_APPLE + " " + userTotal + " " + elapsed + " seconds");

        return userTotal;
    }

    private void setSpecialCities() {
        specialCities.put("Cape Town","Cape Town");
        specialCities.put("Durban","Durban");
        specialCities.put("Sandton","Sandton");
        specialCities.put("Centurion","Centurion");
        specialCities.put("Pretoria","Pretoria");
        specialCities.put("Rustenburg","Rustenburg");
        specialCities.put("Johannesburg","Johannesburg");
        specialCities.put("Klerksdorp","Klerksdorp");
        specialCities.put("Nelspruit","Nelspruit");
        specialCities.put("George","George");
        specialCities.put("Hermanus","Hermanus");
        specialCities.put("Paarl","Paarl");
        specialCities.put("Mossel Bay","Mossel Bay");
        specialCities.put("Fourways","Fourways");
        specialCities.put("Bryanston","Bryanston");
        specialCities.put("Hartbeestpoort","Hartbeestpoort");
        specialCities.put("Brits","Brits");
        specialCities.put("Soweto","Soweto");
        specialCities.put("Mamelodi","Mamelodi");
        specialCities.put("Musina","Musina");
    }

    private final HashMap<String,String> specialCities = new HashMap<>();
    private int processCityUsers(int count, City city) {

        long start = System.currentTimeMillis();
        List<User> cityUsers = new ArrayList<>();

        //
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(firstNames.size() - 1);
            String firstName = firstNames.get(index);
            index = random.nextInt(lastNames.size() - 1);
            String lastName = lastNames.get(index);
            index = random.nextInt(middleInitials.size() - 1);
            String initial = middleInitials.get(index);
            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setCityId(city.getCityId());
            user.setCityName(city.getName());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMiddleInitial(initial);
            user.setDateRegistered(DateTime.now().toDateTimeISO().toString());
            user.setLongDateRegistered(DateTime.now().getMillis());
            cityUsers.add(user);
        }

        userRepo.insert(cityUsers);
        long end = System.currentTimeMillis();
        long elapsed = (end-start)/1000;

        cityCounter++;
        logger.info(E.RED_APPLE + E.RED_APPLE +
                " All " + cityUsers.size() + " users " + E.BLUE_DOT
                + " of city #" + cityCounter +
                " " + city.getName() + ", elapsed: "
                + E.PEAR  + " " + elapsed + " seconds");

        return cityUsers.size();
    }

}
