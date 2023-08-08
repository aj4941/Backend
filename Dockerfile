FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

RUN apt-get update && apt-get install -y python
RUN apt-get update && apt-get install g++
RUN apt-get update && apt-get install openjdk-11-jdk