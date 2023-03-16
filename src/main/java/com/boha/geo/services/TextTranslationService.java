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
            } catch (IOException ex) {
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

        hashMap.put("zh","Chinese");
        hashMap.put("de","German");

//        hashMap.put("sunday","Sunday");
//        hashMap.put("monday","Monday");
//        hashMap.put("tuesday","Tuesday");
//        hashMap.put("wednesday","Wednesday");
//        hashMap.put("thursday","Thursday");
//        hashMap.put("friday","Friday");
//        hashMap.put("saturday","Saturday");
//
//        hashMap.put("af", "Afrikaans");
//        hashMap.put("projectAreas", "Project Areas");
//        hashMap.put("areas", "Areas");
//        hashMap.put("stopMessage", "If you have changed the language of the app please press the stop button and then restart the app to use the new language. If you cancel you will only see the changes after a full stop and start");
//        hashMap.put("restartMessage", "If you have changed the language of the app please press the restart button and then restart the app to use the new language. If you cancel you will only see the changes after a full stop and start");
//        hashMap.put("stop", "Stop");
//        hashMap.put("cancel", "Cancel");
//        hashMap.put("restart", "Restart");
//
//        hashMap.put("weHelpYou", "We help you see more!");
//        hashMap.put("South Africa", "South Africa");
//        hashMap.put("Zimbabwe", "Zimbabwe");
//        hashMap.put("Mozambique", "Mozambique");
//        hashMap.put("Namibia", "Namibia");
//        hashMap.put("Botswana", "Botswana");
//        hashMap.put("Kenya", "Kenya");
//        hashMap.put("Nigeria", "Nigeria");
//        hashMap.put("Democratic Republic of Congo", "Democratic Republic of Congo");
//        hashMap.put("Angola", "Angola");
//
//        hashMap.put("memberLocationResponse", "Member responded to location request");
//        hashMap.put("conditionAdded", "Project condition added");
//
//        hashMap.put("male", "Male");
//        hashMap.put("female", "Female");
//
//        hashMap.put("enterFullName", "Enter Full Name");
//        hashMap.put("enterEmail", "Enter Email Address");
//        hashMap.put("enterCell", "Enter Telephone Number");
//        hashMap.put("submitMember", "Submit Member");
//        hashMap.put("profilePhoto", "Create Profile Photo");
//
//        hashMap.put("enterDescription", "Enter Description");
//        hashMap.put("settingsChanged", "Settings changed or added");
//        hashMap.put("projectAdded", "Project added or changed");
//        hashMap.put("projectLocationAdded", "Project location added");
//        hashMap.put("projectAreaAdded", "Project Area added");
//        hashMap.put("memberAtProject", "Member at Project");
//        hashMap.put("memberAddedChanged", "Member changed or added");
//
//
//        hashMap.put("newProject", "New Project");
//        hashMap.put("enterProjectName", "Enter Project Name");
//
//        hashMap.put("selectPhotoSize", "Select Size of Photos");
//        hashMap.put("fr", "French");
//        hashMap.put("en", "English");
//        hashMap.put("es", "Spanish");
//        hashMap.put("pt", "Portuguese");
//        hashMap.put("zu", "Zulu");
//        hashMap.put("xh", "Xhosa");
//        hashMap.put("sw", "Swahili");
//        hashMap.put("ts", "Tsonga");
//        hashMap.put("st", "Sotho");
//        hashMap.put("yo", "Yoruba");
//        hashMap.put("ig", "Lingala");
//        hashMap.put("noActivities", "No activities happening yet");
//        hashMap.put("tapToRefresh", "Tap to Refresh");
//
//
//        hashMap.put("dashboard", "Dashboard");
//        hashMap.put("loadingActivities", "Loading Activities");
//        hashMap.put("selectLanguage", "Select Language");
//        hashMap.put("pleaseSelectLanguage", "Please Select Language");
//        hashMap.put("small", "Small");
//        hashMap.put("medium", "Medium");
//        hashMap.put("large", "Large");
//        hashMap.put("at", "at");
//        hashMap.put("arrivedAt", "Arrived at $project");
//        hashMap.put("leftFrom", "Leaving $project");
//        hashMap.put("pleaseEnterDistance", "Please enter maximum distance from project in metres");
//        hashMap.put("enterDistance", "Enter maximum distance from project in metres");
//        hashMap.put("pleaseEnterVideoLength", "Please enter maximum video length in seconds");
//        hashMap.put("enterVideoLength", "Enter maximum video length in seconds");
//
//        hashMap.put("maxVideoLength", "Maximum Video Length in Seconds");
//        hashMap.put("maxAudioLength", "Please enter maximum audio length in minutes");
//        hashMap.put("pleaseActivityStreamHours", "Please enter the number of hours your activity stream must show");
//        hashMap.put("activityStreamHours", "Enter the number of hours your activity stream must show");
//        hashMap.put("pleaseNumberOfDays", "Please enter the number of days your dashboard must show");
//        hashMap.put("dashNumberOfDays", "Enter the number of days your dashboard must show");
//        ////
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
//
//        hashMap.put("cellphone", "Cellphone");
//        hashMap.put("fieldMonitor", "Field Monitor");
//        hashMap.put("administrator", "Administrator");
//        hashMap.put("executive", "Executive");
//
//        hashMap.put("pleaseSelectCountry", "Please select Country");
//        hashMap.put("internetConnectionNotAvailable", "Internet Connection not available");
//        hashMap.put("signInFailed", "Sign in failed");
//        hashMap.put("organizationRegistered", "Organization has been registered");
//        //
//        hashMap.put("locationNotAvailable", "Device Location not available");
//        hashMap.put("dataRefreshFailed", "Data refresh failed");
//        hashMap.put("fileSize", "File Size");
//        hashMap.put("duration", "Duration");
//        hashMap.put("videoBuffering", "Video is buffering");
//        hashMap.put("notReadyYet", "Not ready yet");
//        hashMap.put("createAudioClip", "Create Audio Clip");
//        hashMap.put("projectsNotFound", "Projects Not Found'");
//        hashMap.put("projectLocationFailed", "Project Location failed");
//
//        hashMap.put("userCreateFailed", "User Creation failed");
//        hashMap.put("memberCreateFailed", "Member Create failed");
//        hashMap.put("updateFailed", "Update failed");
//        hashMap.put("weGotAProblem", "We got a problem, Sir!");
//        hashMap.put("projectAudio", "Project Audio Clips");
//        hashMap.put("fieldMonitorSchedules", "FieldMonitor Schedules");
//        hashMap.put("audioPlayer", "Audio Player");
//        hashMap.put("welcomeToGeo", "Welcome to Geo!");
//        hashMap.put("projectEditor", "Project Editor");
//        hashMap.put("projectDetails", "Project Details");
//
//        hashMap.put("verifyPhoneNumber", "Verify Phone Number");
//        hashMap.put("phoneNumber", "Phone Number");
//        hashMap.put("projectAddedToOrganization", "{projectName} added to organization");
//        hashMap.put("memberDashboard", "Member Dashboard");
//        hashMap.put("sendCode", "Send Code");
//        hashMap.put("startDate", "Start Date");
//
//        hashMap.put("endDate", "End Date");
//        hashMap.put("numberOfDays", "Number of Days");
//        hashMap.put("projectActivities", "Project Activities");

    }

    public String getEnglishKeys() {
        setLanguageCodes();
        setStrings();

        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Languages: " + codes.size());
        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Strings: " + hashMap.size());
        int cnt = 0;

        List<TranslationBag> translationBags = new ArrayList<>();
        for (String key : hashMap.keySet()) {
            TranslationBag bag = getBag("en", hashMap.get(key), key);
            translationBags.add(bag);
        }
        JSONObject object = new JSONObject();
        for (TranslationBag bag : translationBags) {
            object.put(bag.getKey(), bag.getStringToTranslate());
            cnt++;
            LOGGER.info("%s%sTranslationBag #%d %s%s".formatted(E.AMP, E.AMP, cnt, E.RED_APPLE, G.toJson(bag)));
        }
        String mJson = G.toJson(object);
        Path path
                = Paths.get("en" + ".json");
        try {
            Files.writeString(path, mJson,
                    StandardCharsets.UTF_8);
            LocaleTranslations lts = new LocaleTranslations();
            lts.setLocaleTranslationsId(UUID.randomUUID().toString());
            lts.setDate(DateTime.now().toDateTimeISO().toString());
            lts.setLocale("en");
            lts.setTranslations(mJson);
            localeTranslationsRepository.insert(lts);
            LOGGER.info(E.PINK + E.PINK + E.PINK + " Locale Translations saved for: en");
        } catch (IOException ex) {
            LOGGER.error("Invalid Path");
        }
        return mJson;
    }

    private void setLanguageCodes() {
        codes.add("en");
        codes.add("sn");
        codes.add("yo");
        codes.add("fr");
        codes.add("es");
        codes.add("pt");
        codes.add("af");
        codes.add("zu");
        codes.add("ts");
        codes.add("ig");
        codes.add("nso");
        codes.add("st");
        codes.add("sw");
        codes.add("xh");
        codes.add("zh");
        codes.add("de");
    }
}
