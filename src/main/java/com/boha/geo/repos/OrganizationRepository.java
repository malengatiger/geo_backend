package com.boha.geo.repos;

import com.boha.geo.monitor.data.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrganizationRepository extends MongoRepository<Organization, String> {

    List<Organization> findByCountryId(String countryId);
    Organization findByOrganizationId(String organizationId);

}
