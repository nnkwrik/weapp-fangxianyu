FROM openjdk:8-jre
MAINTAINER nnkwrik nnkwrik@gmail.com

ENV SPRING_PROFILE_ACTIVE=docker

COPY target/auth-service-0.0.1-SNAPSHOT.jar /auth-service.jar

ENTRYPOINT ["java","-jar","/auth-service.jar"]