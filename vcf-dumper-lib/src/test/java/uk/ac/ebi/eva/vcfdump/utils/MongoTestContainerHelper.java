package uk.ac.ebi.eva.vcfdump.utils;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class MongoTestContainerHelper {

    private static final DockerImageName MONGO_IMAGE =
            DockerImageName.parse("percona/percona-server-mongodb:8.0.38-16").asCompatibleSubstituteFor("mongo");

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
