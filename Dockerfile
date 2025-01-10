# Use the latest Gradle image for building the project
FROM gradle:latest AS builder

# Set the working directory in the container
WORKDIR /app

# Copy all project files into the container
COPY . .

# Make the Gradle wrapper executable
RUN chmod +x ./gradlew

# Build the application using Gradle
RUN ./gradlew build --no-daemon

# Use a lightweight OpenJDK image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/snp-mapper-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
