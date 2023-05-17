package com.boha.geo;

import com.boha.geo.services.FirebaseService;
import com.boha.geo.services.MongoService;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.boha.geo.repos")
@Configuration

public class GeoApplication implements ApplicationListener<ApplicationReadyEvent> {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = Logger.getLogger(GeoApplication.class.getSimpleName());
    private static final String alien = E.ALIEN + E.ALIEN + E.ALIEN;

    private final MongoService mongoService;

    private final FirebaseService firebaseService;

    public GeoApplication(MongoService mongoService, FirebaseService firebaseService) {
        this.mongoService = mongoService;
        this.firebaseService = firebaseService;
    }


    public static void main(String[] args) {

        logger.info(alien + " GeoApplication starting ...");
        SpringApplication.run(GeoApplication.class, args);
        logger.info(E.RED_APPLE + E.RED_APPLE + E.RED_APPLE
                + " GeoApplication started OK! " + E.YELLOW + E.YELLOW);
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) throws RuntimeException {

        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
                .getHandlerMethods();

        logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR +
                " Total Endpoints: " + map.size() + "\n");

            firebaseService.initializeFirebase();

        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            var addr = datagramSocket.getLocalAddress().getHostAddress();
            logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR
                    + " datagramSocket: Current IP address : " + addr);
        } catch (SocketException | UnknownHostException e) {
           //
        }

        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR
                    + " Current IP address : " + ip.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {
        return factory -> factory.setContextPath("/geo/v1");
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
        String desc = "This is the backend server API for the Geo Monitoring Service. The service is hosted on Google Cloud Platform and is comprised of " +
                "a set of Java based microservices, authentication and security services as well as push notifications hosted on Firebase. Every user of the" +
                "service is provided a JWT based token which must accompany every request. The data is managed by a MongoDB Atlas database. ";

        String gpt = "Geo is a powerful mobile platform that provides a suite of tools for managing and supervising off-site workers, " +
				"as well as a wide range of other use cases. It allows you to create tasks and assign them to teams, while tracking progress in " +
				"real-time using multimedia files such as photos, videos, and audio recordings. The platform's mobile capabilities enable you to stay " +
				"connected with your team via messaging and video conferencing tools, and monitor progress using a mobile command post. Geo's multimedia " +
				"timeline aggregates all of the relevant data in one place, allowing you to visualize progress and make data-driven decisions. " +
				"With Geo, you can leverage cutting-edge mobile technology to increase productivity, streamline workflows, and achieve better outcomes, " +
				"no matter what kind of project you're working on.";

		String gpt1 = "Geo's platform is built on a robust backend that leverages Google Cloud Platform (GCP) to provide authentication, security, " +
				"scalability, and other critical services. The platform uses MongoDB, a popular NoSQL database, to store and retrieve data, which " +
				"enables it to scale quickly and handle large volumes of multimedia data. Geo also integrates with Firebase Cloud Messaging to provide " +
				"push notifications, which enables near real-time interaction between users and the platform. This allows managers and supervisors to " +
				"monitor progress and communicate with workers in real-time, regardless of their location. The platform's authentication and security " +
				"services ensure that only authorized users can access the data, and that data is protected both in transit and at rest. " +
				"Geo's scalable backend services make it a reliable platform for managing and supervising off-site workers, " +
				"while also providing the flexibility to adapt to the needs of different types of projects and organizations.";

		String m = desc + "\n\n" + gpt + "\n\n" + gpt1;

        return new OpenAPI().info(new Info()
                .title("Geo Monitor API")
                .version(appVersion)
                .description(m)
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0")
                        .url("http://springdoc.org")));
    }


}

