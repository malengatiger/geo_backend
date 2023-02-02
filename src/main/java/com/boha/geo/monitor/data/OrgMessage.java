package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "orgMessages")
public class OrgMessage {
    private String organizationId, projectId, userId, message, created, fcmRegistration;
    private String projectName, adminId, adminName;
    private String frequency, result, name, orgMessageId;
    private OrgMessage replyingTo;


}
