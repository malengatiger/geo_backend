package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "organizations")
public class Organization {
  private String _partitionKey;

    private  String name;
    private String countryName;
    private String countryId;
    private String organizationId;
    private String created;

  public Organization() {
  }

  public Organization(String _partitionKey, String name, String countryName, String countryId, String organizationId, String created) {
    this._partitionKey = _partitionKey;
    this.name = name;
    this.countryName = countryName;
    this.countryId = countryId;
    this.organizationId = organizationId;
    this.created = created;
  }
}
