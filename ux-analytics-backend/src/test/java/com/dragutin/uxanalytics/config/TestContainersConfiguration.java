package com.dragutin.uxanalytics.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.MongoDBContainer;

@Slf4j
@TestConfiguration
public class TestContainersConfiguration {

    public static final MongoDBContainer mongodbContainer;

    static {

        mongodbContainer = new MongoDBContainer("mongo:latest")
                .withExposedPorts(27017);

        mongodbContainer.start();
    }
}
