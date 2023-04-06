package com.boha.geo.controllers;

import com.boha.geo.models.AppError;
import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.services.DataService;
import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.monitor.services.MongoDataService;
import com.boha.geo.monitor.utils.MongoGenerator;
import com.boha.geo.services.CloudStorageUploader;
import com.boha.geo.services.RegistrationService;
import com.boha.geo.services.TextTranslationService;
import com.boha.geo.services.UserBatchService;
import com.boha.geo.util.E;
import com.google.common.io.Files;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController

public class DataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    public DataController(DataService dataService, UserBatchService userBatchService, CloudStorageUploader cloudStorageUploader, MongoGenerator mongoGenerator,
                          MessageService messageService, RegistrationService registrationService, MongoDataService mongoDataService) {
        this.dataService = dataService;
        this.userBatchService = userBatchService;
        this.cloudStorageUploader = cloudStorageUploader;
        this.mongoGenerator = mongoGenerator;
        this.messageService = messageService;
        this.registrationService = registrationService;
        this.mongoDataService = mongoDataService;

        Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();

        LOGGER.info(E.PANDA.concat(E.PANDA) +
                "DataController ready to write data, services injected "
                        .concat(E.PANDA.concat(E.PANDA)));
    }

    Bucket bucket;
    private final DataService dataService;
    private final UserBatchService userBatchService;

    private final CloudStorageUploader cloudStorageUploader;
    private final MongoGenerator mongoGenerator;


    private final MessageService messageService;

    private final RegistrationService registrationService;


    @GetMapping("/ping")
    public String ping() throws Exception {
        return E.HAND2 + E.HAND2 + "PROJECT MONITOR SERVICES PLATFORM pinged at ".concat(new DateTime().toDateTimeISO().toString());
    }

    @Operation(summary = "register Organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization registered",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationRegistrationBag.class))}),

    })
    @PostMapping("/registerOrganization")
    public ResponseEntity<Object> registerOrganization(@RequestBody OrganizationRegistrationBag orgBag) throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS)
                .concat(".... Registering new Organization: ".concat(orgBag.getOrganization().getName())));
        try {
            return ResponseEntity.ok(registrationService.registerOrganization(orgBag));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addOrganization failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "register User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization registered",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),

    })
    @PostMapping("/createUser")
    public ResponseEntity<Object> createUser(@RequestBody User user) throws Exception {

        try {
            return ResponseEntity.ok(dataService.createUser(user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "createUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("/addCountry")
    public ResponseEntity<Object> addCountry(@RequestBody Country country) throws Exception {

        try {
            return ResponseEntity.ok(dataService.addCountry(country));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addCountry failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("/addCommunity")
    public ResponseEntity<Object> addCommunity(@RequestBody Community community) throws Exception {
        try {
            return ResponseEntity.ok(dataService.addCommunity(community));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addCommunity failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("/addCity")
    public ResponseEntity<Object> addCity(@RequestBody City city) throws Exception {
        try {
            City city1 = dataService.addCity(city);
            return ResponseEntity.ok(city1);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addFieldMonitorSchedule failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("/addAppError")
    public ResponseEntity<Object> addAppError(@RequestBody AppError appError) throws Exception {
        try {
            AppError ae = dataService.addAppError(appError);
            return ResponseEntity.ok(ae);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addAppError failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("/addFieldMonitorSchedule")
    public ResponseEntity<Object> addFieldMonitorSchedule(@RequestBody FieldMonitorSchedule fieldMonitorSchedule) throws Exception {
        try {
            FieldMonitorSchedule schedule = dataService.addFieldMonitorSchedule(fieldMonitorSchedule);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addFieldMonitorSchedule failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }


    private final MongoDataService mongoDataService;

    @Operation(summary = "add Project to Organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project added to Organization",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),

    })
    @PostMapping("/addProject")
    public ResponseEntity<Object> addProject(@RequestBody Project project) throws Exception {
        try {
            Project result = dataService.addProject(project);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addProject failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "update Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),

    })
    @PostMapping("/updateProject")
    public ResponseEntity<Object> updateProject(@RequestBody Project project) throws Exception {
        try {
            return ResponseEntity.ok(dataService.updateProject(project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "updateProject failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "send LocationRequest to User via FCM message topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LocationRequest sent",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LocationRequest.class))}),

    })
    @PostMapping("/sendLocationRequest")
    public ResponseEntity<Object> sendLocationRequest(@RequestBody LocationRequest locationRequest) {
        try {
            return ResponseEntity.ok(messageService.sendMessage(locationRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "sendLocationRequest failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @Operation(summary = "send LocationResponse to requester via FCM message topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LocationResponse sent via FCM message topic",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LocationResponse.class))}),

    })
    @PostMapping("/addLocationResponse")
    public ResponseEntity<Object> addLocationResponse(@RequestBody LocationResponse locationResponse) throws Exception {
        try {
            return ResponseEntity.ok(dataService.addLocationResponse(locationResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addLocationResponse failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add organization and/or project Settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Settings added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SettingsModel.class))}),

    })
    @PostMapping("/addSettings")
    public ResponseEntity<Object> addSettings(@RequestBody SettingsModel model) throws Exception {
        try {
            return ResponseEntity.ok(dataService.addSettings(model));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addSettings failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add ProjectPosition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProjectPosition added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectPosition.class))}),

    })
    @PostMapping("/addProjectPosition")
    public ResponseEntity<Object> addProjectPosition(@RequestBody ProjectPosition projectPosition)
            throws Exception {
        try {
            return ResponseEntity.ok(dataService.addProjectPosition(projectPosition));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addProjectPosition failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add ProjectPolygon aka area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProjectPolygon added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectPolygon.class))}),

    })
    @PostMapping("/addProjectPolygon")
    public ResponseEntity<Object> addProjectPolygon(@RequestBody ProjectPolygon projectPolygon)
            throws Exception {
        try {
            return ResponseEntity.ok(dataService.addProjectPolygon(projectPolygon));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addProjectPolygon failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add GeofenceEvent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GeofenceEvent added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GeofenceEvent.class))}),

    })
    @PostMapping("/addGeofenceEvent")
    public ResponseEntity<Object> addGeofenceEvent(@RequestBody GeofenceEvent geofenceEvent)
            throws Exception {
        try {
            return ResponseEntity.ok(dataService.addGeofenceEvent(geofenceEvent));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addGeofenceEvent failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add Photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Photo.class))}),

    })

    @PostMapping("/addPhoto")
    public ResponseEntity<Object> addPhoto(@RequestBody Photo photo) throws Exception {
        try {
            dataService.addPhoto(photo);
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addPhoto failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add LocationRequest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LocationRequest added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LocationRequest.class))}),

    })
    @PostMapping("/addLocationRequest")
    public ResponseEntity<Object> addLocationRequest(@RequestBody LocationRequest request) throws Exception {
        try {
            LocationRequest result = dataService.addLocationRequest(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addLocationRequest failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    //    @PostMapping("/addActivityModel")
//    public ResponseEntity<Object> addActivityModel(@RequestBody ActivityModel model) throws Exception {
//        try {
//             dataService.addActivityModel(model);
//            return ResponseEntity.ok(model);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(
//                    new CustomErrorResponse(400,
//                            "addPhoto failed: " + e.getMessage(),
//                            new DateTime().toDateTimeISO().toString()));
//        }
//
//    }
    @Operation(summary = "add ProjectAssignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProjectAssignment added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectAssignment.class))}),

    })
    @PostMapping("/addProjectAssignment")
    public ResponseEntity<Object> addProjectAssignment(@RequestBody ProjectAssignment projectAssignment) throws Exception {
        try {
            String result = dataService.addProjectAssignment(projectAssignment);
            return ResponseEntity.ok(projectAssignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addProjectAssignment failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add Video")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Video.class))}),

    })
    @PostMapping("/addVideo")
    public ResponseEntity<Object> addVideo(@RequestBody Video video) throws Exception {
        try {
            dataService.addVideo(video);
            return ResponseEntity.ok(video);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addVideo failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add Audio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audio added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Audio.class))}),

    })
    @PostMapping("/addAudio")
    public ResponseEntity<Object> addAudio(@RequestBody Audio audio) throws Exception {
        try {
            dataService.addAudio(audio);
            return ResponseEntity.ok(audio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addAudio failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add Condition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condition added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Condition.class))}),

    })
    @PostMapping("/addCondition")
    public ResponseEntity<Object> addCondition(@RequestBody Condition condition) throws Exception {
        try {
            dataService.addCondition(condition);
            return ResponseEntity.ok(condition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addCondition failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add Message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrgMessage.class))}),

    })
    @PostMapping("/sendMessage")
    public ResponseEntity<Object> sendMessage(@RequestBody OrgMessage orgMessage) throws Exception {
        try {
            OrgMessage result = dataService.addOrgMessage(orgMessage);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "sendMessage failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),

    })
    @PostMapping("/addUser")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(dataService.addUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("/deleteAuthUser")
    public ResponseEntity<Object> deleteAuthUser(@RequestBody String userId) {
        try {
            int result = messageService.deleteAuthUser(userId);
            UserDeleteResponse m = new UserDeleteResponse(result, "User deleted from Firebase Auth");
            return ResponseEntity.ok(m);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "deleteAuthUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }
    }

    @Operation(summary = "update User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),

    })
    @PostMapping("/updateUser")
    public ResponseEntity<Object> updateUser(@RequestBody User user) throws Exception {
        try {
            return ResponseEntity.ok(dataService.updateUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "updateUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("/updateAuthedUser")
    public ResponseEntity<Object> updateAuthedUser(@RequestBody User user) throws Exception {

        try {
            int res = dataService.updateAuthedUser(user);
            UserDeleteResponse r = new UserDeleteResponse(res, "User auth updated");
            return ResponseEntity.ok(r);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "updateAuthedUser failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "add Rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Rating.class))}),

    })
    @PostMapping("/addRating")
    public ResponseEntity<Object> addRating(@RequestBody Rating rating) throws Exception {

        try {
            return ResponseEntity.ok(dataService.addRating(rating));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "addRating failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "create Daily Project Summaries for period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Daily ProjectSummaries added or retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectSummary.class))}),

    })
    @GetMapping("/createDailyProjectSummaries")
    public ResponseEntity<?> createDailyProjectSummaries(@RequestParam String projectId,
                                                         @RequestParam String startDate, @RequestParam String endDate) throws Exception {

        try {
            return ResponseEntity.ok(dataService.createDailyProjectSummaries(projectId, startDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "createDailyProjectSummaries failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "create Hourly Project Summaries for period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Hourly Project Summaries added or retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectSummary.class))}),

    })
    @GetMapping("/createHourlyProjectSummaries")
    public ResponseEntity<?> createHourlyProjectSummaries(@RequestParam String projectId,
                                                          @RequestParam String startDate, @RequestParam String endDate) throws Exception {
        try {
            return ResponseEntity.ok(dataService.createHourlyProjectSummaries(projectId, startDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "createHourlyProjectSummaries failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "create Daily ProjectSummaries for Organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Daily ProjectSummaries added or retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectSummary.class))}),

    })
    @GetMapping("/createDailyOrganizationSummaries")
    public ResponseEntity<?> createDailyOrganizationSummaries(@RequestParam String organizationId,
                                                              @RequestParam String startDate, @RequestParam String endDate) throws Exception {

        try {
            return ResponseEntity.ok(dataService.createDailyOrganizationSummaries(organizationId, startDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "createDailyOrganizationSummaries failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Operation(summary = "create Hourly ProjectSummaries for Organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Hourly ProjectSummaries added or retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectSummary.class))}),

    })
    @GetMapping("/createHourlyOrganizationSummaries")
    public ResponseEntity<?> createHourlyOrganizationSummaries(@RequestParam String organizationId,
                                                               @RequestParam String startDate, @RequestParam String endDate) throws Exception {
        try {
            return ResponseEntity.ok(dataService.createHourlyOrganizationSummaries(organizationId, startDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "createHourlyOrganizationSummaries failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @Autowired
    TextTranslationService textTranslationService;

    @GetMapping("/getEnglishKeys")
    public ResponseEntity<?> getEnglishKeys() throws Exception {
        try {
            String json =textTranslationService.getEnglishKeys();
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "generateTranslations failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/generateTranslations")
    public ResponseEntity<?> generateTranslations() throws Exception {
        try {
            textTranslationService.generateTranslations();
            return ResponseEntity.ok("Translations completed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "generateTranslations failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/getSignedUrl")
    public ResponseEntity<?> getSignedUrl(String objectName, String contentType  ) throws Exception {
        try {
            String url =cloudStorageUploader.getSignedUrl(objectName,contentType);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "fix failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @GetMapping("/deleteTestOrganization")
    public ResponseEntity<?> deleteTestOrganization() throws Exception {
        try {
            dataService.deleteTestOrganization();
            return ResponseEntity.ok("Test Organization deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "fix failed: " + e.getMessage(),
                            new DateTime().toDateTimeISO().toString()));
        }

    }

    @PostMapping("uploadFile")
    public ResponseEntity<Object> uploadFile(
            @RequestParam String objectName,
            @RequestPart MultipartFile document) throws IOException {

        //todo - research sending media files zipped
        String doc = document.getOriginalFilename();
        assert doc != null;
        File file = new File(doc);
        byte[] bytes = document.getBytes();

        Files.write(bytes, file);
        String url = cloudStorageUploader.uploadFile(doc, file);
        boolean ok = file.delete();
        if (ok) {
            LOGGER.info(E.RED_APPLE +E.RED_APPLE +
                    " cloud storage upload file deleted");
        }
        return ResponseEntity.ok(url);
    }

    @PostMapping("uploadMemberFile")
    public ResponseEntity<Object> uploadMemberFile(
            @RequestParam String organizationId,
            @RequestParam String translatedTitle,  @RequestParam String translatedMessage,
            @RequestPart MultipartFile document) throws IOException {

        List<User> users = new ArrayList<>();
        String doc = document.getOriginalFilename();
        if (doc == null) {
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "Problem with user file ",
                            new DateTime().toDateTimeISO().toString()));
        }
        File file = new File(doc);
        Files.write(document.getBytes(), file);
        LOGGER.info("\uD83C\uDF3C\uD83C\uDF3C we have a file: " + file.getName());
        if (file.getName().contains(".csv")) {
            LOGGER.info("\uD83C\uDF3C\uD83C\uDF3C csv file to process: " + file.getName());
            try {
                users = userBatchService.handleUsersFromCSV(file,organizationId,translatedTitle,translatedMessage);
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                        new CustomErrorResponse(400,
                                "Failed to create users: " + e.getMessage(),
                                new DateTime().toDateTimeISO().toString()));
            }
        }
        if (file.getName().contains(".json")) {
            LOGGER.info("\uD83C\uDF3C\uD83C\uDF3C json file to process: " + file.getName());
            try {
                users = userBatchService.handleUsersFromJSON(file,organizationId,translatedTitle,translatedMessage);
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                        new CustomErrorResponse(400,
                                "Failed to create users: " + e.getMessage(),
                                new DateTime().toDateTimeISO().toString()));
            }
        }
        if (file.exists()) {
            boolean ok = file.delete();
            if (ok) {
                LOGGER.info(E.RED_APPLE + E.RED_APPLE +
                        " user batch file deleted");
            }
        }
        if (users.isEmpty()) {
            LOGGER.info("\uD83C\uDF3C\uD83C\uDF3C no users created ... wtf? " );
            return ResponseEntity.badRequest().body(
                    new CustomErrorResponse(400,
                            "Failed to create users; no users in file or file is not .json or .csv ",
                            new DateTime().toDateTimeISO().toString()));
        }
        LOGGER.info("\uD83C\uDF3C\uD83C\uDF3C Users created from batch file: " + users.size());
        return ResponseEntity.ok(users);
    }

    static class UserDeleteResponse {
        public int returnCode;
        public String message;

        public UserDeleteResponse(int returnCode, String message) {
            this.returnCode = returnCode;
            this.message = message;
        }
    }

}
