package com.boha.geo.models;

import com.boha.geo.monitor.data.Position;

public interface GioMediaInterface {
    String getProjectId();
    String getProjectPositionId();
    String getProjectPolygonId();
    String getProjectName();
    String getGeoId();
    String getOrganizationId();
    Position getProjectPosition();
    double getDistanceFromProjectPosition();
    String getUrl();
    String getThumbnailUrl();
    String getCaption();
    String getUserId();
    String getUserName();
    String getCreated();
    String  getUserUrl();
    int getHeight();
    int getWidth();
    int getLandscape();
    String getTranslatedMessage();
    String getTranslatedTitle();
    String getUserType();
}
