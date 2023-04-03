package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("locationRequests")
public class LocationRequest {
    private String organizationId, created, requesterId,
            requesterName;
    private String userId, userName, organizationName;
    private String translatedMessage;
    private String translatedTitle;
}
