FROM maven:3.6.3-openjdk-17-slim AS builder
RUN mkdir /build
WORKDIR /build

COPY ./src ./src
COPY ./pom.xml ./pom.xml
COPY ./.mvn ./.mvn


RUN mvn clean && mvn package

RUN ls . && ls ./target



FROM openjdk:17-jdk-alpine3.14
USER 0
RUN apk update && apk upgrade && mkdir /app

WORKDIR /app

COPY --from=builder /build/target/s3-api.jar ./s3-api.jar

ENTRYPOINT ["java"]

CMD ["-jar", "s3-api.jar"]








