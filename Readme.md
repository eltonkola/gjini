
# ADK agent for curated rss news feeds
<p align="center">
  <img src="https://github.com/eltonkola/gjini/blob/main/media/logo.png" width="200">
</p>
The goal I wanted to achieve was to build an agent that could read news from curated RSS for different countries. 
I mostly care about the USA, Italy and Albania, the three countries that I lived in. For now the agent will 
understand my intent and successfully create a summary of the news. 
Unfortunately the final goal was not achieved, and that is to listen to it on an audio format, like a morning podcast.
The final step would be generating a video of the news, with a virtual anchor reading it for you, on a video format using VEO. 

This project was made for fun during a hackathon (https://googlecloudmultiagents.devpost.com/), and to discover and test the capabilities of the ADK java sdk. Unfortunately it is in the early days, but looks promising.

# Run it
To run the agent locally, set these values on local.properties
```
GOOGLE_GENAI_USE_VERTEXAI=FALSE
GOOGLE_API_KEY=XXX
```
and then run
```
./gradlew runAdkServer
```
on windows, I had to set these values
```
set GOOGLE_GENAI_USE_VERTEXAI=FALSE
set GOOGLE_API_KEY=XXX
```

# Buld it
Build image
```
docker build -t my-adk-server .
```
run it
```
docker run -p 8080:8080 \ # Map ports if your server uses one (e.g., 8080)
-e GOOGLE_API_KEY="your_actual_api_key_here" \
my-adk-server
```


# Notes
Unfortunately image generation and audio generation is not supported yet: https://github.com/google/adk-java/issues/110

