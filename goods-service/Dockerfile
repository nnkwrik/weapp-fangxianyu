FROM openjdk:8-jre
MAINTAINER nnkwrik nnkwrik@gmail.com

ENV SPRING_PROFILE_ACTIVE=docker

COPY target/goods-service-0.0.1-SNAPSHOT.jar /goods-service.jar

ENTRYPOINT ["java","-jar","/goods-service.jar"]