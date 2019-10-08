FROM clojure:openjdk-8-lein-alpine

RUN apk add npm
ADD project.clj /app/
WORKDIR /app/
RUN lein deps
ADD . /app/
RUN lein uberjar

EXPOSE 3000
CMD ["java", "-jar", "/app/target/uberjar/hanzihack.jar"]
