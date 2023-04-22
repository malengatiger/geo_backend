package com.boha.geo.monitor.services;

import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.OrganizationRepository;
import com.boha.geo.repos.ProjectPositionRepository;
import com.boha.geo.repos.ProjectRepository;
import com.boha.geo.repos.UserRepository;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class RegistrationService {
    private static final Logger LOGGER = Logger.getLogger(RegistrationService.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    final OrganizationRepository organizationRepository;
    final UserRepository userRepository;
    final ProjectRepository projectRepository;
    final ProjectPositionRepository projectPositionRepository;
    final DataService dataService;
    final ListService listService;

    public RegistrationService(OrganizationRepository organizationRepository, UserRepository userRepository, ProjectRepository projectRepository, ProjectPositionRepository projectPositionRepository, DataService dataService, ListService listService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectPositionRepository = projectPositionRepository;
        this.dataService = dataService;
        this.listService = listService;
    }

    public OrganizationRegistrationBag registerOrganization(OrganizationRegistrationBag orgBag) throws Exception {

        try {
            Organization org = organizationRepository.insert(orgBag.getOrganization());
            SettingsModel m = dataService.addSettings(orgBag.getSettings());
            String password = orgBag.getUser().getPassword();
            orgBag.getUser().setPassword(null);
            User u = dataService.addUser(orgBag.getUser());
            u.setPassword(password);

            MyProjectBag bag = addSampleProject(org, orgBag.getLatitude(), orgBag.getLongitude());

            OrganizationRegistrationBag registrationBag = new OrganizationRegistrationBag();
            registrationBag.setOrganization(org);
            registrationBag.setSettings(m);
            registrationBag.setUser(u);
            registrationBag.setProject(bag.project);
            registrationBag.setProjectPosition(bag.projectPosition);
            registrationBag.setDate(DateTime.now().toDateTimeISO().toString());


            LOGGER.info(E.LEAF + E.LEAF + " Organization Registered: " + org.getName());
            return registrationBag;
        } catch (Exception e) {
            LOGGER.severe(E.RED_DOT+E.RED_DOT+" We have some kinda problem ...");
            e.printStackTrace();
            throw e;
        }
    }

    private MyProjectBag addSampleProject(Organization organization, double latitude, double longitude) throws Exception {

        Project p0 = new Project();
        p0.setProjectId(UUID.randomUUID().toString());
        p0.setName("Sample Project");
        p0.setCreated(DateTime.now().toDateTimeISO().toString());
        p0.setDescription("Sample Project for learning and practice");
        p0.setOrganizationName(organization.getName());
        p0.setOrganizationId(organization.getOrganizationId());
        p0.setMonitorMaxDistanceInMetres(200);
        List<City> list = listService.findCitiesByLocation(latitude, longitude, 5);
        p0.setNearestCities(list);
        projectRepository.insert(p0);
        LOGGER.info(E.LEAF+E.LEAF+" Sample Organization Project added: " + p0.getName());

        ProjectPosition pPos = new ProjectPosition();
        Position position = new Position();
        position.setType("Point");
        List<Double> mList = new ArrayList<>();
        mList.add(longitude);
        mList.add(latitude);
        position.setCoordinates(mList);

        pPos.setProjectId(p0.getProjectId());
        pPos.setProjectName(p0.getName());
        pPos.setCaption("Project Position Caption");
        pPos.setOrganizationId(organization.getOrganizationId());
        pPos.setProjectPositionId(UUID.randomUUID().toString());
        pPos.setPosition(position);
        pPos.setNearestCities(list);

        projectPositionRepository.insert(pPos);
        LOGGER.info(E.LEAF+E.LEAF+" Sample Organization Project Position added: " + p0.getName());


        LOGGER.info(E.LEAF +
                "Project and ProjectPosition added, project: \uD83C\uDF4E " + p0.getName() + "\t \uD83C\uDF4E " + p0.getOrganizationName());
        MyProjectBag bag = new MyProjectBag(p0,pPos);
        return bag;
    }


    record MyProjectBag(Project project, ProjectPosition projectPosition) {
    }
}
