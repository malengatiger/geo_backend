package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "photos")
public class Photo {
    private String _partitionKey;
    private String projectId, projectPositionId;
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
    private int height, width;


}
