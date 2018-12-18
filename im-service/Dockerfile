FROM openjdk:8-jre
MAINTAINER nnkwrik nnkwrik@gmail.com

ENV SPRING_PROFILE_ACTIVE=docker

COPY target/im-service-0.0.1-SNAPSHOT.jar /im-service.jar

ENTRYPOINT ["java","-jar","/im-service.jar"]