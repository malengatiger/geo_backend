package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "questionnaireResponses")
public class QuestionnaireResponse {
    /*
    private String  _partitionKey;
                                 @Id private String  _id; 
                                 private String  questionnaireResponseId; 
                                 private String  questionnaireId;
                                 private User  user;
                                 private List<Section>  sections;
     */
    private String  _partitionKey;
    private String  questionnaireResponseId;
    private String  questionnaireId;
    private User  user;
    private List<Section> sections;


}
