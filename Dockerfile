FROM openjdk:15-jdk-alpine
VOLUME C:/Users/Jarno/DUO-book-Jarno
ARG JAR_FILE=target/book-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]