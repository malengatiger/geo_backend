package com.boha.geo;

import com.boha.geo.services.FirebaseService;
import com.boha.geo.services.MongoService;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.boha.geo.repos")

public class GeoApplication implements ApplicationListener<ApplicationReadyEvent> {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger logger = Logger.getLogger(GeoApplication.class.getSimpleName());
    private static final String alien = E.ALIEN+E.ALIEN+E.ALIEN;

	@Autowired
	private MongoService mongoService;

	@Autowired
	private FirebaseService firebaseService;


	public static void main(String[] args) {

		logger.info(alien + " GeoApplication starting ...");
		SpringApplication.run(GeoApplication.class, args);
		logger.info(E.RED_APPLE +E.RED_APPLE +E.RED_APPLE
				+ " GeoApplication started OK! " + E.YELLOW+E.YELLOW);
	}


	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.info(alien + " onApplicationEvent, MainApplicationClass: "
				+ event.getSpringApplication().getMainApplicationClass());
		ApplicationContext applicationContext = event.getApplicationContext();
		RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
				.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
				.getHandlerMethods();

//		map.forEach((key, value) -> {
//			logger.info(E.PEAR + E.PEAR +
//					" Endpoint: " + key);
//		});
		logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR +
				" Total Endpoints: " + map.size() + "\n");
		try {

			firebaseService.initializeFirebase();
			//mongoService.printOrganizations();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
