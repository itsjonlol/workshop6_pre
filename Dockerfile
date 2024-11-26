# Use the Maven image as the base
FROM maven:3.9.9-eclipse-temurin-23

# Set up labels for metadata
LABEL MAINTAINER="jonathan"
LABEL DESCRIPTION="vttp ssf day 06 workshop walkthrough"
LABEL version="1.0"
LABEL name="workshop6_pre"

# Define application directory
ARG APP_DIR=/app
WORKDIR ${APP_DIR}

# Copy project files into the image
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY src src

# Ensure the mvnw script has execution permissions
RUN chmod +x mvnw

# Build the application
RUN ./mvnw package -Dmaven.test.skip=true

# Set the server port
ENV SERVER_PORT=4000

# Expose the port
EXPOSE ${SERVER_PORT}

# Run the application
ENTRYPOINT SERVER_PORT=${SERVER_PORT} java -jar target/workshop6_pre-0.0.1-SNAPSHOT.jar

#docker build -t itsjonlol/workshop6_pre:0.0.1 . 
#docker run -d -t -p 4000:4000 itsjonlol/workshop5_pre:0.0.1 