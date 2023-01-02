package com.boha.geo.services;

import com.boha.geo.models.*;
import com.boha.geo.repos.CityPlaceRepo;
import com.boha.geo.repos.CityRepo;
import com.boha.geo.util.E;
import com.boha.geo.util.SecretMgr;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.squareup.okhttp.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.near;

/**
 * Manages the CityPlace resource. Creates CityPlaces on Firestore using the Places API
 */
@Service
public class PlacesService {
    private static final String prefix =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final Logger logger = Logger.getLogger(PlacesService.class.getSimpleName());
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting().create();
    private final SecretMgr secretMgr;
    private final CityRepo cityRepo;
    private final CityPlaceRepo cityPlaceRepo;
    private final MongoClient mongoClient;

    private String apiKey;

    public PlacesService(SecretMgr secretMgr, CityRepo cityRepo, CityPlaceRepo cityPlaceRepo, MongoClient mongoClient) {
        this.secretMgr = secretMgr;
        this.cityRepo = cityRepo;
        this.cityPlaceRepo = cityPlaceRepo;
        this.mongoClient = mongoClient;

        logger.info(E.YELLOW+E.YELLOW+" PlacesService constructed and services injected");
    }

    private String buildLink(double lat, double lng, int radiusInMetres) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append("location=").append(lat).append(",").append(lng);
        sb.append("&radius=").append(radiusInMetres);
        sb.append("&key=").append(apiKey);
        return sb.toString();
    }

    OkHttpClient client = new OkHttpClient();
    int MAX_PAGE_COUNT = 3;
    int pageCount;
    int totalPlaceCount = 0;
    int cityCounter = 0;

    private void getCityPlaces(City city, int radiusInMetres, String pageToken) throws Exception {
        logger.info(E.BLUE_DOT + E.BLUE_DOT + E.BLUE_DOT + E.BLUE_DOT +
                " ... Finding places for " + city.getName() + " radius: " + radiusInMetres);
        if (pageToken == null) {
            if (city.getName().contains("Durban")
                    || city.getName().contains("Pretoria")
                    || city.getName().contains("Cape Town")
                    || city.getName().contains("Johannesburg")
                    || city.getName().contains("Sandton")
                    || city.getName().contains("Bloemfontein")
                    || city.getName().contains("Centurion")
                    || city.getName().contains("Hermanus")) {
                MAX_PAGE_COUNT = 2;
                logger.info(E.RED_DOT + E.RED_DOT + E.RED_DOT +
                        " MAX_PAGE_COUNT = 4 !!! Yay! " + city.getName());
            } else {
                MAX_PAGE_COUNT = 1;
            }
        }

        String link = buildLink(city.getCityLocation().getCoordinates().get(1),
                city.getCityLocation().getCoordinates().get(0), radiusInMetres);
        if (pageToken != null) {
            link += "&pagetoken=" + pageToken;
        }
//        LOGGER.info(E.YELLOW_STAR + E.YELLOW_STAR + " " + link);
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(link).newBuilder();

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        String mResp = response.body().string();
        Root root = gson.fromJson(mResp, Root.class);
        for (CityPlace cityPlace : root.getResults()) {
            cityPlace.setCityId(city.getCityId());
            cityPlace.setCityName(city.getName());
            cityPlace.setProvince(city.getProvince());
            CityPlaceLocation loc = new CityPlaceLocation();
            loc.setType("Point");
            List<Double> list = new ArrayList<>();
            list.add(cityPlace.getGeometry().getLocation().lng);
            list.add(cityPlace.getGeometry().getLocation().lat);
            loc.setCoordinates(list);
            cityPlace.setCityPlaceLocation(loc);
        }
        addCityPlacesToMongo(root, city);
        pageCount++;

        totalPlaceCount += root.getResults().size();
        if (pageCount <= MAX_PAGE_COUNT) {
            if (root.getNextPageToken() != null) {
                getCityPlaces(city, radiusInMetres, root.getNextPageToken());
            }
        }
    }

    private void addCityPlacesToMongo(Root root, City city) throws Exception {

        cityCounter++;
        cityPlaceRepo.insert(root.getResults());

        logger.info(E.RED_APPLE + E.RED_APPLE + " city #" + cityCounter +
                "  - " + city.getName() + " added to db" + E.YELLOW_STAR
        + " with " + E.AMP + " " + root.getResults().size() + " places");
    }

    public List<CityPlace> getPlacesByCityId(String cityId) throws Exception {
        return cityPlaceRepo.findByCityId(cityId);
    }

    public List<CityPlace> getAllPlaces() throws Exception {
        List<CityPlace> cityPlaces = cityPlaceRepo.findAll();
        logger.info(E.CHECK + " getPlaces found: " + cityPlaces.size() + " in Firestore");

        return cityPlaces;
    }

    public List<CityPlace> getPlacesNear(double latitude, double longitude,
                                         double minDistanceInMetres,
                                         double maxDistanceInMetres) throws Exception {
        MongoDatabase mongoDb = mongoClient.getDatabase("geo");
        MongoCollection<Document> cityPlacesCollection = mongoDb.getCollection("cityPlaces");
        Point myPoint = new Point(new Position(longitude, latitude));
        Bson query = near("cityPlaceLocation", myPoint, maxDistanceInMetres, minDistanceInMetres);
        final List<CityPlace> places = new ArrayList<>();

        cityPlacesCollection.find(query)
                .forEach(doc -> {
                    String json = doc.toJson();
                    CityPlace place = gson.fromJson(json, CityPlace.class);
                    places.add(place);
                });

        logger.info(E.PINK+E.PINK+"" + places.size()
                + " places found with min: " + minDistanceInMetres
        + " max: " + maxDistanceInMetres);
        HashMap<String, CityPlace> map = filter(places);
        List<CityPlace> filteredPlaces = map.values().stream().toList();
        int count = 0;
        for (CityPlace place : filteredPlaces) {
            count++;
            logger.info(E.LEAF+E.LEAF+" Place: #" + count + " " + E.RED_APPLE + " " + place.getName()
                    + ", " + place.getCityName());
        }
        return filteredPlaces;

    }

    private static HashMap<String, CityPlace> filter(List<CityPlace> places) {
        HashMap<String,CityPlace> map = new HashMap<>();
        for (CityPlace place : places) {
            if (!map.containsKey(place.getName())) {
                map.put(place.getName(), place);
            }
        }
        return map;
    }

    public PlaceAggregate getPlaceAggregate(String placeId, int minutes) throws Exception {
        List<PlaceAggregate> cityPlaces = new ArrayList<>();


        logger.info(E.RED_APPLE
                + " Events of last " + minutes + " minutes found: " + cityPlaces.size());
//        double totalAmount = 0.0;
//        int totalRating = 0;
//        double avgRating = 0.0;
//
//        for (FlatEvent e : events) {
//            totalAmount += e.getAmount();
//            totalRating += e.getRating();
//        }
//        avgRating = Double.parseDouble("" + (totalRating/events.size()));
//        PlaceAggregate agg = new PlaceAggregate();
//        FlatEvent fe = events.get(0);
//
//        agg.setAverageRating(avgRating);
//        agg.setCityName(fe.getCityName());
//        agg.setCityId(fe.getCityId());
//        agg.setDate(DateTime.now().toDateTimeISO().toString());
//        agg.setLatitude(fe.getLatitude());
//        agg.setLongitude(fe.getLongitude());
//        agg.setPlaceName(fe.getPlaceName());
//        agg.setPlaceId(fe.getPlaceId());
//        agg.setMinutes(minutes);
//        agg.setLongDate(DateTime.now().getMillis());
//        agg.setTotalSpent(totalAmount);
//        agg.setNumberOfEvents(events.size());
//
//
//        LOGGER.info(E.RED_APPLE+
//                " Place Aggregate calculated: " + GSON.toJson(agg));
        return null;
    }

    public CityPlace getPlaceById(String placeId) throws Exception {
        return cityPlaceRepo.findByPlaceId(placeId);
    }
    public List<CityPlace> getPlacesByCityName(String name) throws Exception {
        List<CityPlace> list = cityPlaceRepo.findByCityName(name);
        logger.info(E.PEAR + E.PEAR +
               name +  " - Places found by City name : "  + list.size());
        for (CityPlace place : list) {
            logger.info(" Place: " + E.RED_APPLE + " " + place.getName());
        }
        return list;
    }

    public String loadCityPlaces() throws Exception {
        cityCounter = 9510;
        List<City> cities = cityRepo.findAll();
        Collections.sort(cities);
        apiKey = secretMgr.getPlacesAPIKey();
        //todo remove the shit!
        int START = 9510;
        int index = 0;
        for (City city : cities) {
            if (index > START) {
                pageCount = 0;
                getCityPlaces(city, 15000, null);
            }
            index++;
        }


        return totalPlaceCount + " Total City Places Loaded " + E.AMP + E.AMP + E.AMP;
    }

    public long countPlaces() throws Exception {

        long count = cityPlaceRepo.count();
        logger.info(E.AMP + E.AMP + E.AMP + " Counted " + count + " places");
        return count;
    }
}
