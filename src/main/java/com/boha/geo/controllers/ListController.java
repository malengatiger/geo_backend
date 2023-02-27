package com.boha.geo.controllers;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.monitor.services.MongoDataService;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Duration;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController

public class ListController {
    public static final Logger LOGGER = LoggerFactory.getLogger(ListController.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    Bucket bucket;

    public ListController(ListService listService, MessageService messageService, MongoDataService mongoDataService) {
        this.listService = listService;
        this.messageService = messageService;
        this.mongoDataService = mongoDataService;
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
        LOGGER.info(E.DICE.concat(E.DICE.concat(" ListController ready and able ".concat(E.RED_APPLE))));
    }

    private final ListService listService;
    private final MessageService messageService;

    @GetMapping("/hello")
    public String hello() throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("saying PING! the backend application .... : ".concat(E.FLOWER_YELLOW)));
        return E.HAND2 + E.HAND2 + "PROJECT MONITOR SERVICES PLATFORM says Hi, Nigga! "
                + E.RED_APPLE.concat(new DateTime().toDateTimeISO().toString());
    }

    @PostMapping("/post")
    public String post() throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("testing POST on the backend application .... : ".concat(E.FLOWER_YELLOW)));
        return E.HAND2 + E.HAND2 + "PROJECT MONITOR SERVICES PLATFORM tested POST OK!"
                + E.RED_APPLE.concat(new DateTime().toDateTimeISO().toString());
    }

    @GetMapping("/findUserByEmail")
    public ResponseEntity<Object> findUserByEmail(@RequestParam String email) {
        LOGGER.info(E.DICE.concat(E.DICE).concat("findUserByEmail ... email: ".concat(email)));
        try {
            if (email.isEmpty()) {
                throw new Exception("Email is missing ".concat(E.NOT_OK));
            }
            User user = listService.findUserByEmail(email);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " findUserByEmail found: " + user.getName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findUserByEmail failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizations")
    public ResponseEntity<Object> getOrganizations() {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" getOrganizations ..."));
        try {
            List<Organization> orgs = listService.getOrganizations();
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Organizations found: " + orgs.size());
            return ResponseEntity.ok(orgs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizations failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getCommunities")
    public ResponseEntity<Object> getCommunities() {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" getCommunities ..."));
        try {
            List<Community> communities = listService.getCommunities();
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Communities found: " + communities.size());
            return ResponseEntity.ok(communities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getCommunities failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getProjects")
    public ResponseEntity<Object> getProjects() {
        LOGGER.info(E.DICE.concat(E.DICE).concat("ListController: getProjects ..."));
        try {
            List<Project> projects = listService.getProjects();
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN)
                    + "ListController: Projects found: \uD83D\uDC24 " + projects.size());
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addFieldMonitorSchedule failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }


    private final MongoDataService mongoDataService;

    @GetMapping("/getCities")
    public ResponseEntity<Object> getCities() {
        LOGGER.info(E.DICE.concat(E.DICE).concat("ListController: getCities ..."));
        try {
            List<City> cities = mongoDataService.getCities();
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN)
                    + "ListController: Cities found: \uD83D\uDC24 " + cities.size());
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getCities failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getCitiesByLocation")
    public ResponseEntity<Object> getCitiesByLocation(double latitude, double longitude, int radiusInKM) {
        LOGGER.info(E.DICE.concat(E.DICE).concat("ListController: ...... getCitiesByLocation ..."));
        try {
            List<City> cities = mongoDataService.getCitiesByLocation(new Point(latitude, longitude), new Distance(radiusInKM, Metrics.KILOMETERS));
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN)
                    + "ListController: getCitiesByLocation found: \uD83D\uDC24 " + cities.size());
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getCitiesByLocation failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/findCommunitiesByCountry")
    public ResponseEntity<Object> findCommunitiesByCountry(@RequestParam String countryId) throws Exception {
        LOGGER.info(E.DICE.concat(E.DICE).concat("ListController: findCommunitiesByCountry ..."));
        try {
            List<Community> countries = listService.findCommunitiesByCountry(countryId);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN)
                    + "ListController: findCommunitiesByCountry found: \uD83D\uDC24 " + countries.size());
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findCommunitiesByCountry failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @Operation(summary = "Get all countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the countries",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Country.class))}),

    })
    @GetMapping("/getCountries")

    public ResponseEntity<Object> getCountries() throws Exception {
        LOGGER.info(E.DICE.concat(E.DICE).concat("ListController: findSettlementsByCountry ..."));
        try {
            List<Country> countries = listService.getCountries();
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN)
                    + "ListController: Countries found: \uD83D\uDC24 " + countries.size());
            if (bucket.tryConsume(1)) {
                return ResponseEntity.ok(countries);
            } else {
                LOGGER.error(E.RED_DOT + E.RED_DOT + E.RED_DOT +
                        "Rate limiter got you!");
                throw new Exception("Too many fucking requests");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getCountries failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getCountryOrganizations")
    public ResponseEntity<Object> getCountryOrganizations(@RequestParam String countryId) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" getCountryOrganizations ..."));
        try {
            List<Organization> orgs = listService.getCountryOrganizations(countryId);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Organizations found: " + orgs.size());
            return ResponseEntity.ok(orgs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getCountryOrganizations failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizationProjects")
    public ResponseEntity<Object> getOrganizationProjects(@RequestParam String organizationId) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" getOrganizationProjects ..."));
        try {
            List<Project> projects = listService.getOrganizationProjects(organizationId);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Projects found: " + projects.size());
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationProjects failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getQuestionnairesByOrganization")
    public ResponseEntity<Object> getQuestionnairesByOrganization(@RequestParam String organizationId) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" getOrganizationUsers ..."));
        try {
            List<Questionnaire> users = listService.getQuestionnairesByOrganization(organizationId);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Questionnaires found: " + users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getQuestionnairesByOrganization failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizationUsers")
    public ResponseEntity<Object> getOrganizationUsers(@RequestParam String organizationId) {
        try {
            return ResponseEntity.ok(listService.getOrganizationUsers(organizationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addFieldMonitorSchedule failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizationPhotos")
    public ResponseEntity<Object> getOrganizationPhotos(@RequestParam String organizationId) {
        try {
            return ResponseEntity.ok(listService.getOrganizationPhotos(organizationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationPhotos failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizationVideos")
    public ResponseEntity<Object> getOrganizationVideos(@RequestParam String organizationId) {
        try {
            return ResponseEntity.ok(listService.getOrganizationVideos(organizationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationVideos failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizationAudios")
    public ResponseEntity<Object> getOrganizationAudios(@RequestParam String organizationId) {
        try {
            return ResponseEntity.ok(listService.getOrganizationAudios(organizationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationVideos failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/findProjectsByOrganization")
    public ResponseEntity<Object> findProjectsByOrganization(@RequestParam String organizationId) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findProjectsByOrganization ... id: ".concat(organizationId)));
        try {
            List<Project> users = listService.findProjectsByOrganization(organizationId);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " findProjectsByOrganization found: " + users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findProjectsByOrganization failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Object> getUsers() {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" getUsers ..."));
        try {
            List<User> users = listService.getUsers();
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Users found: " + users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getUsers failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @Operation(summary = "Get a user by their userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),

    })

    @GetMapping("/getUserById")
    public ResponseEntity<?> getUserById(@Parameter(description = "userId of user to be searched") @RequestParam String userId) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" getUserById ..."));
        try {
            User user = listService.getUserById(userId);
            if (user == null) {
                throw new Exception("User is not active; meaning this user is kaput!");
            }
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " User found: " + user.getName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getUserById failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/findProjectsByLocation")
    public ResponseEntity<?> findProjectsByLocation(@RequestParam String organizationId, @RequestParam double latitude,
                                                         @RequestParam double longitude, @RequestParam double radiusInKM) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" ...... findProjectsByLocation ..."));
        try {
            List<Project> projects = listService.findProjectsByLocation(organizationId,latitude, longitude, radiusInKM);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Nearby Projects found: " + projects.size());
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findProjectsByLocation failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/findCitiesByLocation")
    public ResponseEntity<Object> findCitiesByLocation(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radiusInKM) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findCitiesByLocation: ... radiusInKM: " + radiusInKM + " km"));
        try {
            List<City> cities = listService.findCitiesByLocation(latitude, longitude, radiusInKM);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " ....... findCitiesByLocation: cities found: " + cities.size());
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findCitiesByLocation failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/findProjectPositionsByLocation")
    public ResponseEntity<Object> findProjectPositionsByLocation(@RequestParam String organizationId, @RequestParam double latitude,
                                                                 @RequestParam double longitude, @RequestParam double radiusInKM) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findProjectPositionsByLocation ..."));
        try {
            List<ProjectPosition> positions = listService.findProjectPositionsByLocation(organizationId, latitude, longitude, radiusInKM);
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + " Nearby ProjectPositions found: " + positions.size());
            return ResponseEntity.ok(positions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findProjectPositionsByLocation failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getProjectConditions")
    public ResponseEntity<Object> getProjectConditions(String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectConditions ... " + projectId));
        try {
            return ResponseEntity.ok(listService.getProjectConditions(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectConditions failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getProjectPositions")
    public ResponseEntity<Object> getProjectPositions(String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectPositions: " + projectId));
        try {
            return ResponseEntity.ok(listService.getProjectPositions(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectPositions failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getProjectPolygons")
    public ResponseEntity<Object> getProjectPolygons(String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectPositions: " + projectId));
        try {
            return ResponseEntity.ok(listService.getProjectPolygons(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectPolygons failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizationProjectPositions")
    public ResponseEntity<Object> getOrganizationProjectPositions(String organizationId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getOrganizationProjectPositions, organizationId: " + organizationId));
        try {
            return ResponseEntity.ok(listService.getOrganizationProjectPositions(organizationId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationProjectPositions failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    //    @GetMapping("/getGeofenceEventsByUser")
//    public ResponseEntity<Object> getGeofenceEventsByUser(String userId) {
//        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
//                .concat("getGeofenceEventsByUser: " + userId));
//        try {
//            return ResponseEntity.ok(listService.getGeofenceEventsByUser(userId));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(
//                    new CustomErrorResponse(400,
//                            "getGeofenceEventsByUser failed: " + e.getMessage(),
//                            new DateTime().toDateTimeISO().toString()));
//        }
//    }
    @GetMapping("/getGeofenceEventsByProjectPosition")
    public ResponseEntity<Object> getGeofenceEventsByProjectPosition(String projectPositionId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getGeofenceEventsByProjectPosition: " + projectPositionId));
        try {
            return ResponseEntity.ok(listService.getGeofenceEventsByProjectPosition(projectPositionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getGeofenceEventsByProjectPosition failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/countPhotosByProject")
    public ResponseEntity<Object> countPhotosByProject(String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("countPhotosByProject:  starting call: " + projectId));
        try {
            return ResponseEntity.ok(listService.countPhotosByProject(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "countPhotosByProject failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getOrganizationSettings")
    public ResponseEntity<Object> getOrganizationSettings(String organizationId) {
        try {
            return ResponseEntity.ok(listService.getOrganizationSettings(organizationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationSettings failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/countPhotosByUser")
    public ResponseEntity<Object> countPhotosByUser(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("countPhotosByUser:  starting call: " + userId));
        try {
            return ResponseEntity.ok(listService.countPhotosByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "countPhotosByUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/countVideosByProject")
    public ResponseEntity<Object> countVideosByProject(String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("countVideosByProject:  starting call: " + projectId));
        try {
            return ResponseEntity.ok(listService.countVideosByProject(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "countVideosByProject failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }


    }

    @GetMapping("/countVideosByUser")
    public ResponseEntity<Object> countVideosByUser(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("countVideosByUser:  starting call: " + userId));
        try {
            return ResponseEntity.ok(listService.countVideosByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "countVideosByUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "Get project counts of photos, audios, videos etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the project aggregated counts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectSummary.class))}),

    })
    @GetMapping("/getCountsByProject")
    public ResponseEntity<?> getCountsByProject(String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getCountsByProject: " + projectId));
        try {
            return ResponseEntity.ok(listService.getCountsByProject(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getCountsByProject failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/getCountsByUser")
    public ResponseEntity<Object> getCountsByUser(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getCountsByUser: " + userId));
        try {
            return ResponseEntity.ok(listService.getCountsByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getCountsByUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/getProjectPhotos")
    public ResponseEntity<Object> getProjectPhotos(String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectPhotos: " + projectId));
        try {
            return ResponseEntity.ok(listService.getProjectPhotos(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectPhotos failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }
    @GetMapping("/getProjectAssignments")
    public ResponseEntity<Object> getProjectAssignments(String projectId) {

        try {
            return ResponseEntity.ok(listService.getProjectAssignments(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectAssignments failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }
    @GetMapping("/getUserProjectAssignments")
    public ResponseEntity<Object> getUserProjectAssignments(String userId) {

        try {
            return ResponseEntity.ok(listService.getUserProjectAssignments(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getUserProjectAssignments failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }
    @GetMapping("/getOrganizationProjectAssignments")
    public ResponseEntity<Object> getOrganizationProjectAssignments(String organizationId) {

        try {
            return ResponseEntity.ok(listService.getOrganizationProjectAssignments(organizationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationProjectAssignments failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }
    @Operation(summary = "retrieve Organization activity within period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity list retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityModel.class))}),

    })
    @GetMapping("/getOrganizationActivity")
    public ResponseEntity<Object> getOrganizationActivity(@RequestParam String organizationId,
                                                          @RequestParam int hours) {
        try {
            return ResponseEntity.ok(listService.getOrganizationActivity(organizationId, hours));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationActivity failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }
    @Operation(summary = "retrieve Project activity within period ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity list retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityModel.class))}),

    })
    @GetMapping("/getProjectActivity")
    public ResponseEntity<Object> getProjectActivity(@RequestParam String projectId,
                                                          @RequestParam int hours) {
        try {
            return ResponseEntity.ok(listService.getProjectActivity(projectId, hours));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectActivity failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "retrieve User activity within period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity list retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityModel.class))}),

    })
    @GetMapping("/getUserActivity")
    public ResponseEntity<Object> getUserActivity(@RequestParam String userId,
                                                          @RequestParam int hours) {
        try {
            return ResponseEntity.ok(listService.getUserActivity(userId, hours));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getUserActivity failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }
    @Operation(summary = "retrieve Project audio ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity list retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Audio.class))}),

    })
    @GetMapping("/getProjectAudios")
    public ResponseEntity<Object> getProjectAudios(String projectId) {

        try {
            return ResponseEntity.ok(listService.getProjectAudios(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectAudios failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/findAudioById")
    public ResponseEntity<Object> findAudioById(@RequestParam String audioId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("findAudioById: " + audioId));
        try {
            Audio p = listService.findAudioById(audioId);
            if (p == null) {
                return ResponseEntity.badRequest().body(
                        new CustomErrorResponse(401,
                                "audio not found: ",
                                new DateTime().toDateTimeISO().toString()));
            }
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findAudioById failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/findPhotoById")
    public ResponseEntity<Object> findPhotoById(@RequestParam String photoId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("findPhotoById: " + photoId));
        try {
            Photo p = listService.findPhotoById(photoId);
            if (p == null) {
                return ResponseEntity.badRequest().body(
                        new CustomErrorResponse(401,
                                "photo not found: ",
                                new DateTime().toDateTimeISO().toString()));
            }
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findPhotoById failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/findVideoById")
    public ResponseEntity<Object> findVideoById(@RequestParam String videoId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("findVideoById: " + videoId));
        try {
            Video p = listService.findVideoById(videoId);
            if (p == null) {
                return ResponseEntity.badRequest().body(
                        new CustomErrorResponse(401,
                                "video not found: ",
                                new DateTime().toDateTimeISO().toString()));
            }
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "findVideoById failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @GetMapping("/getProjectData")
    public ResponseEntity<Object> getProjectData(@RequestParam String projectId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectData: " + projectId));
        try {
            return ResponseEntity.ok(listService.getProjectData(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectData failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/getOrganizationData")
    public ResponseEntity<Object> getOrganizationData(@RequestParam String organizationId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getOrganizationData: " + organizationId));
        try {
            long start = System.currentTimeMillis();
            DataBag bag = listService.getOrganizationData(organizationId);
            long end = System.currentTimeMillis();
            Double m = Double.valueOf("" + (end - start));
            Double ms = 1000.0;
            double diff = m / ms;
            LOGGER.info(E.RED_DOT + " getOrganizationData: Time elapsed: " + diff + " seconds " + E.RED_DOT);

            return ResponseEntity.ok(bag);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrganizationData failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }
    @Operation(summary = "Get project data from zipped file created by server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the project data, returning byte array",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataBag.class))}),

    })

    @GetMapping(value = "/getProjectDataZippedFile", produces = "application/zip")
    public byte[] getProjectDataZippedFile(@RequestParam String projectId) throws Exception {

        File zippedFile = listService.getProjectDataZippedFile(projectId);
        byte[] bytes = java.nio.file.Files.readAllBytes(zippedFile.toPath());
        boolean deleted = zippedFile.delete();

        LOGGER.info(E.PANDA+E.PANDA + " zipped project file deleted : " + deleted);
        return bytes;
    }

    @Operation(summary = "get Organization Data from a Zipped File")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the data, sending byte array",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataBag.class))}),

    })
    @GetMapping(value = "/getOrganizationDataZippedFile", produces = "application/zip")
    public byte[] getOrganizationDataZippedFile(@RequestParam String organizationId) throws Exception {

        File zippedFile = listService.getOrganizationDataZippedFile(organizationId);
        byte[] bytes = java.nio.file.Files.readAllBytes(zippedFile.toPath());
        boolean deleted = zippedFile.delete();

        LOGGER.info(E.PANDA+E.PANDA + " zipped organization file deleted : " + deleted);
        return bytes;
    }

    @Operation(summary = "get data created byUser ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataBag.class))}),

    })
    @GetMapping("/getUserData")
    public byte[] getUserData(@RequestParam String userId) throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getUserData: " + userId));
        File zippedFile = listService.getUserDataZippedFile(userId);
        byte[] bytes = java.nio.file.Files.readAllBytes(zippedFile.toPath());
        boolean deleted = zippedFile.delete();

        LOGGER.info(E.PANDA+E.PANDA + " zipped user file deleted : " + deleted);
        return bytes;

    }

    @Operation(summary = "retrieve Project Photos created by User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Photos retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Photo.class))}),

    })
    @GetMapping("/getUserProjectPhotos")
    public ResponseEntity<Object> getUserProjectPhotos(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getUserProjectPhotos: " + userId));
        try {
            return ResponseEntity.ok(listService.getUserProjectPhotos(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getUserProjectPhotos failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "retrieve Project Videos created by User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Videos retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Video.class))}),

    })
    @GetMapping("/getUserProjectVideos")
    public ResponseEntity<Object> getUserProjectVideos(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getUserProjectVideos: " + userId));
        try {
            return ResponseEntity.ok(listService.getUserProjectVideos(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getUserProjectVideos failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "retrieve Project Audio created by User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audios retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Audio.class))}),

    })
    @GetMapping("/getUserProjectAudios")
    public ResponseEntity<Object> getUserProjectAudios(String userId) {
        try {
            return ResponseEntity.ok(listService.getUserProjectAudios(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getUserProjectAudios failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "retrieve Project video created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audios retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Video.class))}),

    })
    @GetMapping("/getProjectVideos")
    public ResponseEntity<Object> getProjectVideos(String projectId)
            throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectVideos: " + projectId));
        try {
            return ResponseEntity.ok(listService.getProjectVideos(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectVideos failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "retrieve Project schedules created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FieldMonitorSchedule.class))}),

    })
    @GetMapping("/getProjectFieldMonitorSchedules")
    public ResponseEntity<Object> getProjectFieldMonitorSchedules(String projectId)
            throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectFieldMonitorSchedules: " + projectId));
        try {
            return ResponseEntity.ok(listService.getProjectFieldMonitorSchedules(projectId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectFieldMonitorSchedules failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/getUserFieldMonitorSchedules")
    public ResponseEntity<Object> getUserFieldMonitorSchedules(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getProjectFieldMonitorSchedules: " + userId));
        try {
            return ResponseEntity.ok(listService.getUserFieldMonitorSchedules(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getProjectFieldMonitorSchedules failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/getMonitorFieldMonitorSchedules")
    public ResponseEntity<Object> getMonitorFieldMonitorSchedules(String userId)
            throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getMonitorFieldMonitorSchedules: " + userId));
        try {
            return ResponseEntity.ok(listService.getMonitorFieldMonitorSchedules(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getMonitorFieldMonitorSchedules failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/getOrgFieldMonitorSchedules")
    public ResponseEntity<Object> getOrgFieldMonitorSchedules(String organizationId)
            throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat("getOrgFieldMonitorSchedules: " + organizationId));
        try {
            return ResponseEntity.ok(listService.getOrgFieldMonitorSchedules(organizationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getOrgFieldMonitorSchedules failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "find Cities within geographic area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cities found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = City.class))}),

    })
    @GetMapping("/getNearbyCities")
    public ResponseEntity<Object> getNearbyCities(double latitude, double longitude, double radiusInKM) {
        try {
            return ResponseEntity.ok(listService.getNearbyCities(latitude, longitude, radiusInKM));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "getNearbyCities failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

}
