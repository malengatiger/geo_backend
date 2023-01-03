package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "geofenceEvents")
public class GeofenceEvent {

       private String status, geofenceEventId,
               date, projectPositionId,
       projectId,
       projectName, userId;
       private User user;
       private Position position;


}
