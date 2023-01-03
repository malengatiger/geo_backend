package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "answers")
public class Answer {
    /*

     */
    private String _partitionKey;
    @Id
    private String _id;
    private String text;
    private double number;
    private List<String> photoUrls;
    private List<String> videoUrls;

    public Answer() {
    }


}
