package com.boha.geo.repos;

import com.boha.geo.monitor.data.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<Country, String> {
}
