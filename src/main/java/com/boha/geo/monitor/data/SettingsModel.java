package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("settings")
public class SettingsModel {
    private int distanceFromProject;
    private int photoSize;
    private int maxVideoLengthInMinutes;
    private int maxAudioLengthInMinutes;
    private int themeIndex;
    private int activityStreamHours;
    private String settingsId, created, organizationId, projectId;
}
