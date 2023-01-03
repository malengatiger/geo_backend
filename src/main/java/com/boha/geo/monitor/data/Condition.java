package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "conditions")
public class Condition {
    private String _partitionKey;
    @Id
    private String _id;
    private String projectId, organizationId, projectPositionId;
    private String projectName;
    private Position projectPosition;
    private int rating;
    private String caption;
    private String userId;
    private String userName;
    private String created;
    private String conditionId;

    public Condition() {
    }

}
