# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/todo-0.0.1-SNAPSHOT.jar todo-app.jar

# Expose the application's port (default Spring Boot port)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "todo-app.jar"]