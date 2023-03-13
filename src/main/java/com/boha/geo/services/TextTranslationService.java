package com.boha.geo.services;

// Imports the Google Cloud Translation library.

import com.boha.geo.monitor.data.LocaleTranslations;
import com.boha.geo.repos.LocaleTranslationsRepository;
import com.boha.geo.util.E;
import com.google.cloud.translate.v3.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class TextTranslationService {
    static final Logger LOGGER = LoggerFactory.getLogger(TextTranslationService.class);
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Value("${projectId}")
    private String projectId;
    private final LocaleTranslationsRepository localeTranslationsRepository;

    TranslationServiceClient translationServiceClient;

    public TextTranslationService(LocaleTranslationsRepository localeTranslationsRepository) {
        this.localeTranslationsRepository = localeTranslationsRepository;
    }

    private void initialize() throws Exception {
        try {
            translationServiceClient = TranslationServiceClient.create();
        } catch (Exception e) {

        }
    }

    public String translateText(TranslationBag bag) throws Exception {

        if (translationServiceClient == null) {
            initialize();
        }

        LocationName parent = LocationName.of(projectId, "global");

        TranslateTextRequest request =
                TranslateTextRequest.newBuilder()
                        .setParent(parent.toString())
                        .setMimeType("text/plain")
                        .setTargetLanguageCode(bag.getTarget())
                        .addContents(bag.getStringToTranslate())
                        .build();

        TranslateTextResponse response = translationServiceClient.translateText(request);

        // Display the translation for each input text provided
        String translatedText = null;
        for (Translation translation : response.getTranslationsList()) {
            translatedText = translation.getTranslatedText();
        }

        return translatedText;

    }

    public void generateTranslations() throws Exception {
        setLanguageCodes();
        setStrings();
        DateTime start = DateTime.now();
        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Languages: " + codes.size());
        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Strings: " + hashMap.size());
        int cnt = 0;
        for (String code : codes) {
            List<TranslationBag> translationBags = new ArrayList<>();
            for (String key : hashMap.keySet()) {
                TranslationBag bag = getBag(code, hashMap.get(key), key);
                translationBags.add(bag);
            }
            JSONObject object = new JSONObject();
            for (TranslationBag bag : translationBags) {
                String text = translateText(bag);
                bag.setTranslatedText(text);
                object.put(bag.getKey(), bag.getTranslatedText());
                cnt++;
                LOGGER.info("%s%sTranslationBag #%d %s%s".formatted(E.AMP, E.AMP, cnt, E.RED_APPLE, G.toJson(bag)));
                try {
                    LOGGER.info(" ..... sleeping for 1 seconds ....");
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            String mJson = G.toJson(object);
            Path path
                    = Paths.get("intl_" + code + ".json");
            try {
                Files.writeString(path, mJson,
                        StandardCharsets.UTF_8);
                LocaleTranslations lts = new LocaleTranslations();
                lts.setLocaleTranslationsId(UUID.randomUUID().toString());
                lts.setDate(DateTime.now().toDateTimeISO().toString());
                lts.setLocale(code);
                lts.setTranslations(mJson);
                localeTranslationsRepository.insert(lts);
                LOGGER.info(E.PINK + E.PINK + E.PINK + " Locale Translations saved for: " + code);
            }
            catch (IOException ex) {
                LOGGER.error("Invalid Path");
            }


        }

        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Translations done: " + cnt);

        DateTime end = DateTime.now();
        long ms = end.getMillis() - start.getMillis();
        double delta = Double.parseDouble("" + ms) / Double.parseDouble("1000");

        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of TranslationBags: " + bags.size() +
                " elapsed time: " + delta + " seconds");
        translationServiceClient.close();
    }

    private TranslationBag getBag(String code, String stringToTranslate, String key) {
        TranslationBag bag = new TranslationBag();
        bag.setStringToTranslate(stringToTranslate);
        bag.setSource("en");
        bag.setTarget(code);
        bag.setFormat("text");
        bag.setKey(key);
        return bag;
    }

    List<String> codes = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();
    List<TranslationBag> bags = new ArrayList<>();

    private void setStrings() {
        hashMap.put("locationNotAvailable", "Device Location not available");
        hashMap.put("dataRefreshFailed", "Data refresh failed");
        hashMap.put("fileSize", "File Size");
        hashMap.put("duration", "Duration");
        hashMap.put("videoBuffering", "Video is buffering");
        hashMap.put("notReadyYet", "Not ready yet");
        hashMap.put("createAudioClip", "Create Audio Clip");
        hashMap.put("projectsNotFound", "Projects Not Found'");
        hashMap.put("projectLocationFailed", "Project Location failed");

        hashMap.put("userCreateFailed", "User Creation failed");
        hashMap.put("memberCreateFailed", "Member Create failed");
        hashMap.put("updateFailed", "Update failed");
        hashMap.put("weGotAProblem", "We got a problem, Sir!");
        hashMap.put("projectAudio", "Project Audio Clips");
        hashMap.put("fieldMonitorSchedules", "FieldMonitor Schedules");
        hashMap.put("audioPlayer", "Audio Player");
        hashMap.put("welcomeToGeo", "Welcome to Geo!");
        hashMap.put("projectEditor", "Project Editor");
        hashMap.put("projectDetails", "Project Details");

        hashMap.put("verifyPhoneNumber", "Verify Phone Number");
        hashMap.put("phoneNumber", "Phone Number");
        hashMap.put("projectAddedToOrganization", "{projectName} added to organization");
        hashMap.put("memberDashboard", "Member Dashboard");
        hashMap.put("sendCode", "Send Code");
        hashMap.put("startDate", "Start Date");

        hashMap.put("endDate", "End Date");
        hashMap.put("numberOfDays", "Number of Days");
        hashMap.put("projectActivities", "Project Activities");

//        hashMap.put("registerOrganization", "Register Organization");
//        hashMap.put("organizationDashboard", "Organization Dashboard");
//        hashMap.put("projectDashboard", "Project Dashboard");
//        hashMap.put("projectLocationsMap", "Project Locations Map");
//        hashMap.put("refreshProjectDashboardData", "Refresh Project Dashboard Data");
//        hashMap.put("photosVideosAudioClips", "Photos, Videos and Audio Clips");
//        hashMap.put("photos", "Photos");
//        hashMap.put("videos", "Videos");
//        hashMap.put("audioClips", "Audio Clips");
//        hashMap.put("addProjectLocations", "Add Project Locations");
//        hashMap.put("addProjectAreas", "Add Project Areas");
//        hashMap.put("editProject", "Edit Project");
//        hashMap.put("directionsToProject", "Directions to Project");
//        hashMap.put("addProjectLocationHere", "Add Project Location Here");
//        hashMap.put("organizationMembers", "Organization Members");
//        hashMap.put("organizationProjects", "Organization Projects");
//        hashMap.put("newMember", "New Member");
//        hashMap.put("editMember", "Edit Member");
//        hashMap.put("administratorsMembers", "Administrators & Members");
//        hashMap.put("tapForColorScheme", "Tap for Color Scheme");
//        hashMap.put("fieldMonitorInstruction", "The Field Monitor members that are working with projects must follow the limits described below when they are making photos, videos and audio clips");
//        hashMap.put("maximumMonitoringDistance", "Maximum Monitoring Distance in metres");
//        hashMap.put("maximumVideoLength", "Maximum Video Length in seconds");
//        hashMap.put("maximumAudioLength", "Maximum Audio Length in minutes");
//        hashMap.put("activityStreamHours", "Activity Stream in hours");
//        hashMap.put("numberOfDaysForDashboardData", "Number of days for Dashboard data");
//        hashMap.put("selectSizePhotos", "Select size of photos");
//        hashMap.put("selectProjectIfNecessary", "Select project only if these settings are for a single project, otherwise the settings are for the entire organization");
//        hashMap.put("projectName", "Project Name");
//        hashMap.put("descriptionOfProject", "Description of the Project");
//        hashMap.put("submitProject", "Submit Project");
//        hashMap.put("requestMemberLocation", "Request Member Location");
//        hashMap.put("projects", "Projects");
//        hashMap.put("members", "Members");
//
//        hashMap.put("locations", "Locations");
//        hashMap.put("areas", "Areas");
//        hashMap.put("schedules", "Schedules");
//        hashMap.put("january", "January");
//        hashMap.put("february", "February");
//        hashMap.put("march", "March");
//        hashMap.put("april", "April");
//        hashMap.put("may", "May");
//        hashMap.put("june", "June");
//        hashMap.put("july", "July");
//        hashMap.put("august", "August");
//        hashMap.put("september", "September");
//        hashMap.put("october", "October");
//        hashMap.put("november", "November");
//        hashMap.put("december", "December");
//        hashMap.put("activityTitle", "Activities in the last $count hours");
//        hashMap.put("dashboardSubTitle", "Data represents the last $count days");
//        hashMap.put("settings", "Settings");
//        hashMap.put("callMember", "Call Member");
//        hashMap.put("sendMemberMessage", "Send Member Message");
//        hashMap.put("removeMember", "Remove Member");
//        hashMap.put("name", "Name");
//        hashMap.put("emailAddress", "Email Address");
//        hashMap.put("male", "Male");
//        hashMap.put("female", "Female");
//        hashMap.put("cellphone", "Cellphone");
//        hashMap.put("fieldMonitor", "Field Monitor");
//        hashMap.put("administrator", "Administrator");
//        hashMap.put("executive", "Executive");
//        hashMap.put("submitMember", "Submit Member");
//        hashMap.put("profilePhoto", "Profile Photo");
//        hashMap.put("pleaseSelectCountry", "Please select Country");
//        hashMap.put("internetConnectionNotAvailable", "Internet Connection not available");
//        hashMap.put("signInFailed", "Sign in failed");
//        hashMap.put("organizationRegistered", "Organization has been registered");

    }

    private void setLanguageCodes() {
        //codes.add("en");
        codes.add("fr");
        codes.add("es");
        codes.add("pt");
        codes.add("af");

        codes.add("zu");
        codes.add("ts");
        codes.add("ig");
        codes.add("nso");
        codes.add("st");
        codes.add("sn");
        codes.add("sw");
        codes.add("xh");
    }
}
