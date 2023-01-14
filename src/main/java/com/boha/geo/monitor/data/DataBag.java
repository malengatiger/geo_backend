package com.boha.geo.monitor.data;

import lombok.Data;

import java.util.List;

@Data
public class DataBag {
    private List<Photo> photos;
    private List<Video> videos;
    private List<FieldMonitorSchedule> fieldMonitorSchedules;
    private List<ProjectPosition> projectPositions;
    private List<ProjectPolygon> projectPolygons;

    private List<Project> projects;
    private List<User> users;
    private String date;

}
