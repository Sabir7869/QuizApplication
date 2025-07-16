# Use official Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy all files to container
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JDK to run the app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/quizApp-0.0.1-SNAPSHOT.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
