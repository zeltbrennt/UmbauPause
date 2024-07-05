# buildstep
FROM gradle:8.8.0-jdk17 as BUILD

WORKDIR /webshop
COPY ./gradlew ./gradlew
COPY ./gradle.properties ./gradle.properties
COPY ./build.gradle.kts ./build.gradle.kts
COPY ./settings.gradle.kts ./settings.gradle.kts
COPY ./src ./src
RUN gradle build

# runstep
FROM openjdk:17-slim
COPY --from=BUILD /webshop/build/libs/*.jar /webshop/app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/webshop/app.jar"]