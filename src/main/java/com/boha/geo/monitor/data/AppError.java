package com.boha.geo.monitor.data;

import com.boha.geo.monitor.data.Position;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("appErrors")
public class AppError {
    private String errorMessage;
    private String model;
    private String created;
    private String userId;
    private String userName;
    private Position errorPosition;
    private String iosName;
    private String versionCodeName;
    private String manufacturer;
    private String brand;
    private String organizationId;
    private String baseOS;
    private String deviceType;
    private String userUrl;
    private String uploadedDate;
    private String iosSystemName;
}
