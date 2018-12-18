FROM openjdk:8-jre
MAINTAINER nnkwrik nnkwrik@gmail.com

ENV SPRING_PROFILE_ACTIVE=docker

COPY target/gateway-0.0.1-SNAPSHOT.jar /gateway.jar

ENTRYPOINT ["java","-jar","/gateway.jar"]