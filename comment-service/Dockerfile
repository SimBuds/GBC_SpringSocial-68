# Build stage as a gradle image (Alpine is a portable linux image)
FROM gradle:8-jdk17-alpine AS builder

# Copy local code to the container image.
COPY --chown=gradle:gradle . /home/gradle/src

# Telling docker to use gradle as the user
WORKDIR /home/gradle/src

# Build a release artifact.
RUN gradle build -x test

# Package Stage

FROM openjdk:17-jdk-alpine

# Copy the jar to the production image from the builder stage.
RUN mkdir /app

# Copy the jar to the production image from the builder stage.
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/user-service.jar

# Enviroment variables
ENV ENV SPRING_DATASOURCE_USERNAME=postgres \
    ENV SPRING_DATASOURCE_PASSWORD=mypass

# Port
EXPOSE 8086

# Entrypoint for the container
ENTRYPOINT ["java","-jar","/app/user-service.jar"]