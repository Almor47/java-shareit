FROM amazoncorretto:11
COPY target/*.jar server.jar
ENV TZ=Europe/Moscow
ENTRYPOINT ["java", "-jar", "/server.jar"]
