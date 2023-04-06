package com.boha.geo.monitor.data;

import com.boha.geo.models.GioMediaInterface;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "audios")
public class Audio implements GioMediaInterface {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String  projectId;
    private String projectPositionId;
    private String projectPolygonId;
    private String  projectName;
    private String  audioId;
    private String  organizationId;
    private Position  projectPosition;
    private double  distanceFromProjectPosition;
    private String  url;
    private String  caption;
    private String  userId;
    private String  userName;
    private String  userUrl;
    private String  created;
    private int  durationInSeconds = 0;
    private String translatedMessage;
    private String translatedTitle;
    private String userType;


    @Override
    public String getGeoId() {
        return audioId;
    }

    @Override
    public String getThumbnailUrl() {
        return null;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getLandscape() {
        return 0;
    }
}
