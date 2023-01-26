package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "geofenceEvents")
public class GeofenceEvent {
    private String _partitionKey;
    @Id
    private String _id;

    private String status, geofenceEventId,
            date, projectPositionId,
            projectId,
            projectName, organizationId;
    private User user;
    private Position position;


}
