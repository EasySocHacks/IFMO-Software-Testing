FROM maven
COPY /src /api/src
COPY /pom.xml /api/pom.xml
RUN mvn -f /api/pom.xml package -DskipTests

FROM openjdk
RUN ls
RUN ls /tagter
COPY /target/spring-0.0.1-SNAPSHOT.jar api/spring-0.0.1-SNAPSHOT.jar
WORKDIR "/api"
CMD java -jar spring-0.0.1-SNAPSHOT.jar