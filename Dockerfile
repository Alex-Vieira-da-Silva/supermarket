FROM amazoncorretto:17-alpine

RUN apk add --no-cache curl

ENV TZ=America/Recife

WORKDIR /app

COPY target/supermarket-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
