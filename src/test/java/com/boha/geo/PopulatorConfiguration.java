package com.boha.geo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
public class PopulatorConfiguration {

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getCountries() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{new ClassPathResource("testData/countries.json")});

        return factory;
    }
    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getUsers() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{new ClassPathResource("testData/users.json")});

        return factory;
    }
}
