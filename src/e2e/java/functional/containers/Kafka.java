package functional.containers;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class Kafka extends KafkaContainer {

    private static final String IMAGE_VERSION = "5.4.3";
    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse(IMAGE_VERSION);
    private static Kafka container;

    private Kafka() {
    }

    public static Kafka getInstance(){
        if(container == null)
            container = new Kafka();

        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("KAFKA_BOOTSTRAP_SERVERS", container.getBootstrapServers());
    }
}
