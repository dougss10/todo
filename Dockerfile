FROM openjdk:17-jdk-slim

WORKDIR /app
COPY target/*.jar /app/api.jar

EXPOSE 8080

ARG DB_HOST_ARG
ARG DB_USER_ARG
ARG DB_PASS_ARG
ENV DB_HOST=$DB_HOST_ARG
ENV DB_USER=$DB_USER_ARG
ENV DB_PASS=$DB_PASS_ARG

CMD ["java", "-jar", "api.jar"]