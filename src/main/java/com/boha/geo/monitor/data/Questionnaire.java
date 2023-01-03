package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "questionnaires")
public class Questionnaire {

    private String _partitionKey;
    private String organizationId;
    private String created;
    private String questionnaireId;
    private String title;
    private String projectId;
    private String description;
    private List<Section> sections;


}
