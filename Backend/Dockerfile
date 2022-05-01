FROM maven AS build
RUN mkdir -p /backend
WORKDIR /backend
COPY pom.xml /backend
COPY  src /backend/src
RUN mvn -f pom.xml clean
RUN mvn install -DskipTests

FROM openjdk:11
COPY --from=build /backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]