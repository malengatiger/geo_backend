package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ratings")
public class Rating {
    private String _partitionKey;
    @Id
    private String _id;
    private String ratingId,projectId;
    private String projectName;
    private int ratingCode;
    private String created;
    private String userId, userName;
    private String organizationId;
    private String videoId, audioId, photoId, remarks;
    private Position position;

    public Rating() {
    }

}
