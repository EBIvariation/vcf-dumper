package uk.ac.ebi.eva.vcfdump.utils;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public abstract class MongoTestContainerHelper {

    private static final String MONGO_IMAGE = "mongo:6.0";

    @ServiceConnection
    public static MongoDBContainer mongo = new MongoDBContainer(MONGO_IMAGE);

    static {
        mongo.start();
    }

    @DynamicPropertySource
    public static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host",
                () -> mongo.getHost() + ":" + mongo.getMappedPort(27017));
        registry.add("spring.data.mongodb.authenticationDatabase", () -> "");
        registry.add("spring.data.mongodb.username", () -> "");
        registry.add("spring.data.mongodb.password", () -> "");
    }
}
