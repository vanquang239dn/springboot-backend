FROM eclipse-temurin:21-jdk

ARG JAR_FILE=target/backend-service.jar

COPY ${JAR_FILE} backend-service.jar

ENTRYPOINT ["java", "-jar", "backend-service.jar"]

EXPOSE 8080