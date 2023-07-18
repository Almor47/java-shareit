FROM amazoncorretto:11
COPY target/*.jar gateway.jar
ENV TZ=Europe/Moscow
ENTRYPOINT ["java","-jar","/gateway.jar"]
