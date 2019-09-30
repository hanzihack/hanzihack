FROM openjdk:8-alpine

COPY target/uberjar/hanzihack.jar /hanzihack/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/hanzihack/app.jar"]
