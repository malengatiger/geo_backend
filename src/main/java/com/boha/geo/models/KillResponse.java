package com.boha.geo.models;

import com.boha.geo.monitor.data.User;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "killResponses")
public class KillResponse {
    private User user;
    private User killer;
    private String message;
    private String organizationId;
    private String date;

}
