package com.boha.geo.repos;

import com.boha.geo.monitor.data.LocaleTranslations;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocaleTranslationsRepository extends MongoRepository<LocaleTranslations, String> {


}
