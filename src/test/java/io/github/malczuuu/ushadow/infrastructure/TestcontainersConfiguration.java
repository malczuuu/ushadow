package io.github.malczuuu.ushadow.infrastructure;

import static io.github.malczuuu.ushadow.infrastructure.ContainerVersions.MONGO_IMAGE;
import static io.github.malczuuu.ushadow.infrastructure.ContainerVersions.RABBITMQ_IMAGE;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

  @ServiceConnection
  @Bean
  public MongoDBContainer mongoDbContainer() {
    return new MongoDBContainer(DockerImageName.parse(MONGO_IMAGE));
  }

  @ServiceConnection
  @Bean
  public RabbitMQContainer rabbitMQContainer() {
    return new RabbitMQContainer(DockerImageName.parse(RABBITMQ_IMAGE));
  }
}
