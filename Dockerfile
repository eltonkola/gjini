FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN chmod +x ./gradlew

RUN ./gradlew createCustomDistribution --no-daemon


FROM openjdk:17-jdk-slim
EXPOSE 8080

RUN mkdir /app

COPY --from=build /build/app-dist/app.jar /app/app.jar
COPY --from=build /build/app-dist/lib /app/lib/

COPY src/main/kotlin /app/src/main/kotlin

ENV GOOGLE_GENAI_USE_VERTEXAI="FALSE"

ENTRYPOINT ["java"]
CMD ["-cp", "/app/app.jar;/app/lib/*", \
     "com.google.adk.web.AdkWebServer", \
     "--adk.agents.source-dir=/app/src/main/kotlin", \
     "--adk.agents.package=com.eltonkola", \
     "--debug"]