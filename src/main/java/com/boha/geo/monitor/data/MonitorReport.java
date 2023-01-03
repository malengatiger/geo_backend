package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*

 */
@Data
@Document(collection = "monitorReports")
public class MonitorReport {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String  monitorReportId;
    private String  projectId;
    private User  user;
    private String rating;
    private List<Photo> photos;
    private List<Video>  videos;
    private String  description;
    private String  created;
    private Position  position;

    public MonitorReport() {
    }

}
