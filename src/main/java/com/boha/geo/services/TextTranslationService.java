package com.boha.geo.services;

// Imports the Google Cloud Translation library.

import com.boha.geo.monitor.data.LocaleTranslations;
import com.boha.geo.monitor.data.TranslationBag;
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
        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Languages: " + languageCodes.size());
        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Strings: " + hashMap.size());
        int cnt = 0;
        for (String languageCode : languageCodes) {
            List<TranslationBag> translationBags = new ArrayList<>();
            for (String key : hashMap.keySet()) {
                TranslationBag bag = getBag(languageCode, hashMap.get(key), key);
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
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            String mJson = G.toJson(object);
            Path path
                    = Paths.get("intl_" + languageCode + ".json");
            try {
                Files.writeString(path, mJson,
                        StandardCharsets.UTF_8);
                LocaleTranslations lts = new LocaleTranslations();
                lts.setLocaleTranslationsId(UUID.randomUUID().toString());
                lts.setDate(DateTime.now().toDateTimeISO().toString());
                lts.setLocale(languageCode);
                lts.setTranslations(mJson);
                localeTranslationsRepository.insert(lts);
                LOGGER.info(E.PINK + E.PINK + E.PINK + " Locale Translations saved for: " + languageCode);
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

    private TranslationBag getBag(String languageCode, String stringToTranslate, String key) {
        TranslationBag bag = new TranslationBag();
        bag.setStringToTranslate(stringToTranslate);
        bag.setSource("en");
        bag.setTarget(languageCode);
        bag.setFormat("text");
        bag.setKey(key);
        return bag;
    }

    List<String> languageCodes = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();
    List<TranslationBag> bags = new ArrayList<>();

    //DO NOT REMOVE!!!!
    private void addBigStrings() {

        String m1 = "Geo is a powerful tool to monitor the construction and maintenance of " +
                "infrastructure and facilities, especially in areas where youth unemployment is high " +
                "and corruption may be an issue. With Geo, they can track progress and quality in real-time, " +
                "ensure that projects are being completed on time and within budget, and " +
                "identify and address potential issues before they become major problems. \n" +
                "By leveraging Geo\s multimedia monitoring capabilities, the agency can also " +
                "improve transparency and accountability, which can help combat corruption and " +
                "build trust with the community. \nUltimately, Geo can help the agency maximize the impact " +
                "of its investments in infrastructure and other facilities, " +
                "while also creating opportunities for unemployed youth to participate in monitoring " +
                "and contributing to the development of their communities.";
        
        hashMap.put("infrastructure", m1);
        
        String m2 = "Given the prevalence of smartphones and digital media use among young people, " +
                "Geo could be a valuable tool for engaging and employing youth in community development projects. " +
                "Field worker positions could be specifically targeted towards youth who are comfortable " +
                "using smartphones and digital media, and who may have been previously unemployed. " +
                "Furthermore, involvement with Geo could provide youth with valuable job skills " +
                "and work experience, which could increase their employability in the future. \n" +
                "In addition, by engaging with youth and involving them in community development projects, " +
                "Geo could help to foster a sense of pride and ownership in their communities, " +
                "which could lead to increased engagement and participation in the future.";

        hashMap.put("youth", m2);

        String m3 = "Government agencies and officials can benefit from Geo in several ways. " +
                "First, Geo can help in the efficient and effective management of infrastructure projects " +
                "by providing real-time multimedia monitoring and reporting, which can help identify and " +
                "address any issues that may arise during the project. Second, Geo can help to combat corruption " +
                "by producing an objective and verifiable record of project progress and expenses, " +
                "which can help to prevent or detect fraudulent activities. \n" +
                "Third, Geo can provide valuable insights into the needs and priorities of communities, " +
                "which can help government agencies to better target their resources and efforts to where " +
                "they are needed most. Finally, by engaging and " +
                "empowering young people in the process of monitoring and reporting on infrastructure projects, " +
                "Geo can help to build a sense of ownership and responsibility among youth, while also providing them " +
                "with valuable skills and experience that can help to improve their employment prospects.";
        
        hashMap.put("govt", m3);
        
        String m4 = "Community groups can get involved with Geo by using the platform to " +
                "monitor and manage their community-based projects, such as community clean-up campaigns, " +
                "volunteer initiatives, or fundraising events. The platform can also be used to track the " +
                "progress of these projects and ensure that resources are being allocated effectively. " +
                "Additionally, community groups can use Geo to report incidents or issues in their community, " +
                "such as crime or environmental hazards, and track the response of relevant authorities. \n" +
                "This can help to improve community safety and promote greater collaboration between community members " +
                "and local government agencies. Finally, community groups can also use Geo to coordinate volunteer " +
                "efforts during emergencies, such as natural disasters, and ensure that resources are being distributed to those in need.";

        hashMap.put("community", m4);
        
    }

    private void setStrings() {

        hashMap.put("events","Events");
        hashMap.put("recentEvents","Recent Events");

//        hashMap.put("uploadMemberBatchFile","Upload Member File");
//        hashMap.put("pickMemberBatchFile","Pick Member file for upload");
//        hashMap.put("memberUploadFailed","Member File upload failed, members not created");
//        hashMap.put("downloadExampleFiles","Download Member example files");
//        hashMap.put("uploadInstruction","Multiple members may be uploaded from either a spreadsheet in csv format or a json file. You can download example files in each format");

//        hashMap.put("memberProfileUploaded","Member profile picture uploaded and database updated");
//        hashMap.put("memberProfileUploadFailed","Member profile picture upload failed. Please try again later");

//        hashMap.put("memberProfilePicture","Member Profile Picture");
//        hashMap.put("useCamera","Use Camera");
//        hashMap.put("pickFromGallery","Pick from Photo Gallery");
//        hashMap.put("profileInstruction","Please set up your profile picture. You can use an existing photo or take a new one with the camera.");

//        hashMap.put("audioArrived","An audio clip from the field has arrived.");
//        hashMap.put("photoArrived","A photograph from the field has arrived.");
//        hashMap.put("videoArrived","A video clip from the field has arrived.");
//
//        hashMap.put("locationRequestArrived","A request for location arrived.");
//        hashMap.put("locationResponseArrived","A response to location request has arrived.");
//
//        hashMap.put("memberAddedChanged","A member has been added or modified.");
//
//        hashMap.put("projectAdded","Project $project has been added.");
//        hashMap.put("settingsArrived","New project settings have arrived.");
//
//        hashMap.put("projectAreaAdded","A project area has been added to $project");
//        hashMap.put("projectPositionAdded","A project location has been added to $project");
//


//        hashMap.put("geoRunning","$geo service is running.");
//        hashMap.put("tapToReturn","Tap to return to $geo");

//        hashMap.put("messageFromGeo","Message arrived from $geo");

//        hashMap.put("phoneAuth","Phone Authentication");
//        hashMap.put("emailAuth","Email Authentication");
//        hashMap.put("signInInstruction","Please sign in using the appropriate method. " +
//                "It is recommended to use phone authentication if your device can accept SMS messages. " +
//                "If your device does not support SMS messaging please use email authentication");
//        hashMap.put("enterPassword","Enter Password");
//        hashMap.put("password","Password");
//        hashMap.put("signInOK","Sign in succeeded!");

//        hashMap.put("photoLocation","Photo was taken at this location");
//        hashMap.put("videoLocation","Video was made at this location");
//        hashMap.put("audioLocation","Audi clip was recorded at this location");
//        hashMap.put("memberLocation","Member responded with their location");

//        hashMap.put("signIn","Sign In");
//        hashMap.put("memberSignedIn","Member has been signed in");
//        hashMap.put("putInCode","Please put in the code that was sent to you");
//        hashMap.put("duplicateOrg","Duplicate organization name");
//        hashMap.put("memberNotExist","This member does not exist in the database");
//        hashMap.put("serverUnreachable","server cannot be reached");
//        hashMap.put("phoneSignIn","Phone Sign In");
//        hashMap.put("phoneAuth","Phone Authentication");
//        hashMap.put("enterPhone","Enter Phone Number");
//        hashMap.put("phoneNumber","Phone Number");
//
//        hashMap.put("verifyPhone","Verify Phone Number");
//        hashMap.put("enterSMS","Enter SMS pin code sent to your device");
//        hashMap.put("sendCode","Send Code");
//        hashMap.put("verifyComplete","Verification completed. Thank you!");
//        hashMap.put("verifyFailed","Verification failed. Please try later");
//        hashMap.put("enterOrg","Enter Organization Name");
//
//        hashMap.put("orgName","Organization Name");
//
//        hashMap.put("enterAdmin","Enter Administrator Name");
//        hashMap.put("adminName","Administrator Name");
//        hashMap.put("enterEmail","Enter Email Address");
//        hashMap.put("emailAddress","Email Address");

//        hashMap.put("Democratic Republic of Congo","Democratic Republic of Congo");
//        hashMap.put("Lesotho","Lesotho");
//        hashMap.put("Eswatini","Eswatini");

//        hashMap.put("pleaseSelectGender","Please select Member gender");
//        hashMap.put("pleaseSelectType","Please select Member type");
//        hashMap.put("memberCreated","Member created");
//        hashMap.put("memberCreateFailed","Member creation failed");
//        hashMap.put("memberUpdateFailed","Member update failed");

//        hashMap.put("af","Afrikaans");
//          hashMap.put("initializing","Initializing your device database and other resources. Please wait for a few seconds.");

//        hashMap.put("maxVideoLessThan", "The maximum video length should be less than $count seconds");
//        hashMap.put("maxAudioLessThan", "The maximum audio clip length should be less than $count minutes");

//        hashMap.put("schedules", "Schedules");
//        hashMap.put("monitorSchedules", "Field Monitor Schedules");
//        hashMap.put("serverNotAvailable", "Server is not available at this time. Please try again later");
//        hashMap.put("checkSettings", "Please check your device WIFI and network settings");
//        hashMap.put("createNewProject", "Please create a new project to get work started");
//        hashMap.put("addNewMembers", "Please register new members for your organization");

//        hashMap.put("memberArrived", "A Member has arrived or signed in at $project");
//        hashMap.put("memberLeft", "A Member has left the $project");

//        hashMap.put("addAudioRating", "Add Audio Rating");
//        hashMap.put("addVideoRating", "Add Video Rating");
//        hashMap.put("addPhotoRating", "Add Photo Rating");
//        hashMap.put("audioRatings", "Audio Clip Ratings");
//        hashMap.put("videoRatings", "Video Ratings");
//        hashMap.put("photoRatings", "Photo Ratings");
//        hashMap.put("errorRecording", "Error encountered during recording");




//        hashMap.put("createdAt", "Date created");
//        hashMap.put("createdBy", "Created by ");

//
//        hashMap.put("waitingToRecordAudio", "Waiting to record audio clip");
//        hashMap.put("waitingToRecordVideo", "Waiting to record video");


//        hashMap.put("networkProblem", "There is a possible network problem at this time. Please try again later.");
//        hashMap.put("serverProblem", "The server is not available at this time. Please try again later.");
//        hashMap.put("createProject", "Please create a new project");
//        hashMap.put("createOrg", "Please create new organization");
//        hashMap.put("checkNetworkSettings", "Please check your device network settings");
//        hashMap.put("createMembers", "Please create new members in your organization");
//        hashMap.put("portraitMode", "It is recommended that you take your pictures and videos in portrait mode. It works much better than landscape mode");

//        hashMap.put("videosNotFoundInProject", "Videos not found in project $project");
//        hashMap.put("photosNotFoundInProject", "Photos not found in project $project");
//        hashMap.put("audiosNotFoundInProject", "Audio clips not found in project $project");
//        hashMap.put("projectsNotFoundInOrg", "Projects not found in Organization");



//        hashMap.put("videoToBeUploaded", "Video will be uploaded soon");
//        hashMap.put("photoToBeUploaded", "Photo will be uploaded soon");
//        hashMap.put("audioToBeUploaded", "Audio clip will be uploaded soon");
//        hashMap.put("videoLimitReached", "Video recording limit of $count seconds has been reached");
//        hashMap.put("audioLimitReached", "Audio clip recording limit of $count minutes has been reached");
//        hashMap.put("welcomeAboard", "Welcome aboard Geo!");

//        hashMap.put("managementPeople", "Managers, Administrators and Supervisors");
//        hashMap.put("fieldWorkers", "Field Monitors and other workers");
//        hashMap.put("executives", "Upper Level Management and Executives");
//        hashMap.put("organizations", "Organizations");
//        hashMap.put("information", "Information");
//        hashMap.put("government", "Government");
//        hashMap.put("communityTitle", "Communities");
//        hashMap.put("thankYou", "Thank You");
//        hashMap.put("thankYouMessage", "Thank you for even getting to this point. Your time and effort is much appreciated and we hope you enjoy your journeys with this app!");


//        addBigStrings();

//        hashMap.put("projectActivity", "Project Activity");
//        hashMap.put("memberActivity", "Member Activity");

//        hashMap.put("memberArrivedAtProject", "Member $member arrived at project $project");
//        hashMap.put("memberLeftProject", "Member $member left from project $project");
//        hashMap.put("organizationActivity", "Organization Activity");

//        hashMap.put("gettingCameraReady", "Getting camera ready");
//        hashMap.put("recordingComplete", "Recording complete");
//        hashMap.put("recordingLimitReached", "Recording limit reached");
//        hashMap.put("duration", "Duration");
//        hashMap.put("fileSavedWillUpload", "File is saved and will be uploaded soon");
//        hashMap.put("problemWithCamera", "Problem with camera");
//        hashMap.put("locationRequestReceived", "A location request has been received");
//        hashMap.put("locationResponseReceived", "A location response has been received");


//        hashMap.put("projectLocatedHere","Project is located here");
//        hashMap.put("projectMultipleLocations","Project has multiple locations");
//        hashMap.put("projectAreas","Project has one or more areas");
//        hashMap.put("refreshData","Refresh data from server");




//        hashMap.put("takePicture","Take Picture");
//        hashMap.put("takePhoto","Take Photo");
//        hashMap.put("elapsedTime","Elapsed Time");
//        hashMap.put("recordAudioClip","Record Audio Clip");
//        hashMap.put("playAudioClip","Play Audio Clip");
//        hashMap.put("uploadAudioClip","Upload Audio Clip");
//        hashMap.put("playVideo","Play Video");
//        hashMap.put("recordVideo","Record Video");
//        hashMap.put("uploadVideo","Upload Video");


//        hashMap.put("projectLocationsAreas","Project Locations and Areas");
//        hashMap.put("projectMonitoringAreas","Project Monitoring Areas");
//        hashMap.put("organizationLocationsAreas","Organizations Locations and Areas");
//        hashMap.put("location","Location $count");
//        hashMap.put("area","Area $count");
//        hashMap.put("noPhotosInProject","No photos found in project");
//        hashMap.put("noAudioInProject","No audio clips found in project");
//        hashMap.put("noVideosInProject","No videos found in project");
//        hashMap.put("settingSaved","Settings have been saved");
//        hashMap.put("appInfo","App Information");
//        hashMap.put("whatGeoDo","What does Geo do?");
//        hashMap.put("geoForOrg","Geo for Organizations");
//        hashMap.put("geoForManagers","Geo for Managers and Coordinators");
//        hashMap.put("infoForManagers","Information for Mangers and Coordinators");
//        hashMap.put("infoForMonitors","Information for Field Monitors");
//        hashMap.put("infoForMonitors","Information for Executives and Owners");
//        hashMap.put("howToStart","How to start");
//        hashMap.put("thankYou","Thank You!");
//        hashMap.put("thankYouMessage","Thank you for even getting to this point. " +
//                "Your time and effort is much appreciated and we hope you enjoy your journeys " +
//                "with this app!");
//        hashMap.put("networkUnavailable","Network Unavailable");
//        hashMap.put("messageReceived","Message received");

//        hashMap.put("zh","Chinese");
//        hashMap.put("de","German");

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
//        hashMap.put("projectsNotFound", "Projects Not Found");
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

        LOGGER.info(E.PINK + E.PINK + E.PINK + " Number of Languages: " + languageCodes.size());
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
        languageCodes.add("en");
        languageCodes.add("af");
        languageCodes.add("fr");
        languageCodes.add("es");
        languageCodes.add("pt");
        languageCodes.add("de");
        languageCodes.add("sn");
        languageCodes.add("yo");
        languageCodes.add("zu");
        languageCodes.add("ts");
        languageCodes.add("ig");
        languageCodes.add("st");
        languageCodes.add("sw");
        languageCodes.add("xh");
        languageCodes.add("zh");

    }
}
