FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/transferMoney-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java", "-jar", "/myapp.jar"]