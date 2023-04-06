package com.boha.geo.monitor.data;

import com.boha.geo.models.GioMediaInterface;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "photos")
public class Photo implements GioMediaInterface {
    private String _partitionKey;
    private String projectId;
    private String projectPositionId;
    private String projectPolygonId;
    private String projectName;
    private String photoId;
    private String organizationId;
    private Position projectPosition;
    private double distanceFromProjectPosition;
    private String url;
    private String thumbnailUrl;
    private String caption;
    private String userId;
    private String userName;
    private String created;
    private String  userUrl;
    private int height;
    private int width;
    private int landscape;
    private String translatedMessage;
    private String translatedTitle;
    private String userType;

    @Override
    public String getGeoId() {
        return photoId;
    }
}
