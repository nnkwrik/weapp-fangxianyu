FROM openjdk:8-jre
MAINTAINER nnkwrik nnkwrik@gmail.com

ENV SPRING_PROFILE_ACTIVE=docker

COPY target/eureka-0.0.1-SNAPSHOT.jar /eureka.jar

ENTRYPOINT ["java","-jar","/eureka.jar"]