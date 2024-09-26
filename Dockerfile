FROM adoptopenjdk/openjdk10
ENV home /app/bgapp
WORKDIR ${home}
COPY target/*.jar bg.jar
EXPOSE 8080
CMD ["java", "-jar", "bg.jar"]