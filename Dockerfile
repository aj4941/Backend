FROM openjdk:17-oracle

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

COPY temp.cpp /codes/temp.cpp
COPY temp.py /codes/temp.py
COPY Main.java /codes/Main.java

ENTRYPOINT ["java","-jar","/app.jar"]

