package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("locationResponses")

public class LocationResponse {
    @Id
    private String _id;
    private String organizationId, userId, userName, date, organizationName, locationResponseId;
    private Position position;
}
