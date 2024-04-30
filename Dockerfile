FROM openjdk:17-alpine

ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} application.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
