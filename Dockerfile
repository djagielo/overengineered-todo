FROM openjdk:15-jdk-alpine

WORKDIR /app

COPY /build/libs/ambicion-service-*.jar /app/ambicion-service.jar

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "ambicion-service.jar"]