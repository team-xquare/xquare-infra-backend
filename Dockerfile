FROM openjdk:17-alpine

RUN apk add --no-cache curl py-pip \
    && pip install --upgrade pip \
    && pip install awscli

ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} application.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "application.jar"]


