FROM java:8-jdk-alpine
VOLUME /tmp
ADD ./build/libs/weatherForecast-bulletin-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
