FROM amazoncorretto:17-alpine

ENV TZ=America/Recife

WORKDIR /app

COPY supermarket-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]