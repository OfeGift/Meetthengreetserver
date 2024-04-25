FROM openjdk:17
EXPOSE 8080
RUN mkdir /app
COPY ./build/libs/*-all.jar /app/meetthengreet-server.jar
ENTRYPOINT ["java", "-jar","/app/meetthengreet-server.jar"]