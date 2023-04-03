package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "users")
public class User {
    String _partitionKey;
    @Id
    String _id;
    String name;
    String gender;
    String email;
    String cellphone;
    String userId;
    String countryId;
    String organizationId;
    String organizationName;
    String created;
    String fcmRegistration;
    String userType;
    String password;
    Position position;
    int active = 0;
    String updated;
    String imageUrl;
    String thumbnailUrl;
    private String translatedMessage;
    private String translatedTitle;


}
