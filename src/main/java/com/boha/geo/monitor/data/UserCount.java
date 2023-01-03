package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "userCounts")

public class UserCount {
    /*
    data class UserCount(var userId: String,  var photos: Int = 0, var videos: Int = 0, var projects:Int = 0) {
}

     */
    private String userId;
    private int photos;
    private int videos;
    private int projects;
    private String date, organizationId;
    private User user;


}
