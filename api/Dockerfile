FROM maven AS build
WORKDIR "/api"
COPY /src /src
COPY /pom.xml /pom.xml
RUN mvn -f /pom.xml package -DskipTests

FROM openjdk
COPY --from=build /target/spring-0.0.1-SNAPSHOT.jar /api/spring-0.0.1-SNAPSHOT.jar
WORKDIR "/api"
CMD java -jar spring-0.0.1-SNAPSHOT.jar