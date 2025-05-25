FROM ubuntu:latest AS builderstage

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /project
COPY . .
RUN ./gradlew createCustomDistribution --no-daemon


FROM openjdk:17-jdk-slim
EXPOSE 8080

WORKDIR /app

# Correctly copy artifacts from the 'builderstage'
COPY --from=builderstage /project/build/app-dist/app.jar /app/app.jar
COPY --from=builderstage /project/build/app-dist/lib /app/lib/

# This still copies from the host machine's context where `docker build` is run
# This is correct if you want to ship the sources separate from the build stage artifacts.
COPY src/main/kotlin /app/src/main/kotlin

ENV GOOGLE_GENAI_USE_VERTEXAI="FALSE"
ENV APP_CLASSPATH "/app/app.jar:/app/lib/*"

# Consider setting user and group for security (optional but good practice)
# RUN groupadd -r appgroup && useradd -r -g appgroup appuser
# USER appuser

ENTRYPOINT ["java"]
CMD ["-cp", "${APP_CLASSPATH}", \
     "com.google.adk.web.AdkWebServer", \
     "--adk.agents.source-dir=/app/src/main/kotlin", \
     "--adk.agents.package=com.eltonkola", \
     "--debug"]