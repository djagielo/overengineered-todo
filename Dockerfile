FROM openjdk:15-jdk-alpine

WORKDIR /app

COPY /build/libs/overengineered-todo-*.jar /app/overengineered-todo.jar

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "overengineered-todo.jar"]