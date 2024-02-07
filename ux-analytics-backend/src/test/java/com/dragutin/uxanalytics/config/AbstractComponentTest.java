package com.dragutin.uxanalytics.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import(TestContainersConfiguration.class)
@TestPropertySource(locations="classpath:application-test.yaml")
public class AbstractComponentTest {

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {

        registry.add("mongodb.container.port=", () -> TestContainersConfiguration.mongodbContainer.getMappedPort(27017));
    }
}
