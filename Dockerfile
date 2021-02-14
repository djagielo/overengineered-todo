FROM maven:3.6.3-openjdk-15-slim as MAVEN_BUILD

WORKDIR /build

COPY pom.xml .

COPY .mvn .

COPY src ./src

RUN mvn clean package

FROM openjdk:15

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/ambicion-service-*.jar /app/ambicion-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ambicion-service.jar"]