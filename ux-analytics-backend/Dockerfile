FROM openjdk:17-jdk-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package -DskipTests=true

FROM openjdk:17-jdk-slim
WORKDIR demo
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]