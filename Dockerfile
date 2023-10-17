FROM openjdk:17-oracle

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

COPY src/main/resources/codes/temp.cpp /codes/temp.cpp
COPY src/main/resources/codes/temp.py /codes/temp.py
COPY src/main/resources/codes/temp.java /codes/temp.java

ENTRYPOINT ["java","-jar","/app.jar"]
