FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/project-data-provider-0.0.1-SNAPSHOT.jar project-data-provider-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/project-data-provider-0.0.1-SNAPSHOT.jar"]