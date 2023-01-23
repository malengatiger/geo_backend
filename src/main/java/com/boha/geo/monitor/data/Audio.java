package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "audios")
public class Audio {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String  projectId, projectPositionId, projectPolygonId;
    private String  projectName;
    private String  audioId;
    private String  organizationId;
    private Position  projectPosition;
    private double  distanceFromProjectPosition;
    private String  url;
    private String  caption;
    private String  userId;
    private String  userName;
    private String  created;
    private int  durationInSeconds = 0;


}
