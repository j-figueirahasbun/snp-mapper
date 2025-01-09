# Use the latest OpenJDK runtime as a parent image
FROM gradle:latest

# Install xargs (included in findutils) 
#RUN apt-get update && apt-get install -y findutils

# Set the working directory in the container 
WORKDIR /app 

# Copy the Gradle wrapper files 
COPY gradlew /app/ 
COPY gradle /app/gradle 

# Copy the rest of the source code 
COPY . /app 

# Move to the directory containing the main entry point 
#WORKDIR /app/api/src/main/

# Make the gradlew script executable 
RUN chmod +x /app/gradlew 

# Build the application 
RUN ./gradlew build

# Expose port 8080 
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "api/build/libs/snp-mapper-0.0.1-SNAPSHOT.jar"]