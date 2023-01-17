package com.boha.geo.monitor.data;

import lombok.Data;

import java.util.List;

@Data
public class OrganizationRegistrationBag {
    private Organization organization;
    private Project sampleProject;
    private ProjectPosition sampleProjectPosition;
    private String date;
    private List<User> sampleUsers;
    private double latitude;
    private double longitude;
}
