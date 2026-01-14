FROM amazoncorretto:17
ENV TZ=America/Recife
WORKDIR /app
COPY supermarket-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]