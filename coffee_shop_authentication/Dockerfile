FROM maven:3.9.4-amazoncorretto-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM amazoncorretto:17-alpine3.15-jdk
MAINTAINER authen_user
COPY --from=build /home/app/target/coffee_shop_authentication-latest.jar coffee_shop_authentication-latest.jar
ENTRYPOINT java -jar /coffee_shop_authentication-latest.jar