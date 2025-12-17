FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy project metadata first to leverage Docker layer caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
RUN chmod +x mvnw
RUN ./mvnw -B -q -DskipTests dependency:go-offline

# Copy source and build the application
COPY src src
RUN ./mvnw -B -q -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine AS run
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
