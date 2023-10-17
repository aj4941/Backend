FROM openjdk:17-oracle

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

COPY src/resources/codes/temp.cpp /codes/temp.cpp
COPY src/resources/codes/temp.py /codes/temp.py
COPY src/resources/codes/Main.java /codes/Main.java

ENTRYPOINT ["java","-jar","/app.jar"]
