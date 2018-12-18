FROM openjdk:8-jre
MAINTAINER nnkwrik nnkwrik@gmail.com

ENV SPRING_PROFILE_ACTIVE=docker

COPY target/user-service-0.0.1-SNAPSHOT.jar /user-service.jar

ENTRYPOINT ["java","-jar","/user-service.jar"]