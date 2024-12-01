# Use an official OpenJDK base image
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built by Maven into the container
COPY target/*.jar app.jar

# Expose the application port (adjust based on your app's configuration)
EXPOSE 8080

# Command to run the app (adjust this based on the name of your .jar file)
CMD ["java", "-jar", "/app/app.jar"]