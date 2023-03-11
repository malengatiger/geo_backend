package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("activities")
public class ActivityModel {

        String _id;
        String activityModelId;
        ActivityType activityType;
        String date;
        String userId;
        String userName, userThumbnailUrl;
        String projectId;
        String projectName;
        String organizationName;
        String organizationId;
        Photo photo;
        Video video;
        Audio audio;
        User user;
        Project project;
        ProjectPosition projectPosition;
        ProjectPolygon projectPolygon;
        OrgMessage orgMessage;
        GeofenceEvent geofenceEvent;
        LocationRequest locationRequest;
        LocationResponse locationResponse;
}
