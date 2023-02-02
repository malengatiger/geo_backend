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
    String name, gender;
    String email;
    String cellphone;
    String userId, countryId;
    String organizationId;
    String organizationName;
    String created, fcmRegistration;
    String userType;
    String password;
    Position position;
    int active = 0;
    String updated, imageUrl, thumbnailUrl;


}
