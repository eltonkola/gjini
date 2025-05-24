package com.eltonkola

import com.eltonkola.news.RssRepository
import com.google.adk.agents.LlmAgent
import com.google.adk.tools.Annotations
import com.google.adk.tools.FunctionTool

class RssSourcesAgent {

    companion object {

            @JvmField
            val rssAgent = LlmAgent.builder()
                    .model("gemini-2.0-flash")
                    .name("rss_agent")
                .description("Agent than finds a set of rss feeds from a location.")
                .instruction(
                    """
                    You are a helpful news agent. Given a location such as a city, state, or country,
                    determine the country it belongs to and normalize the country name to a lowercase full name.
                    
                    Examples of normalization:
                    - "united states", "new york", "california" → "usa"
                    - "madrid" → "spain"
                    - "tirana" → "albania"
                    
                    Then, call the tool `getRssFeeds` with the normalized country name to retrieve the list of RSS feed sources.
                    
                    Only use country names present in the RSS directory. The user deos not need to know these resources, another agent will 
                    extract and provide them the news content .
            """.trimIndent()
                )
                    .tools(
                        FunctionTool.create(RssSourcesAgent::class.java, "getRssFeeds")
                    )
                    .build()


            @JvmStatic
            fun getRssFeeds(
                @Annotations.Schema(description = "Country name") country: String? = null
            ): Map<String, Any> {
                return try {

                    val feeds = RssRepository.getFeeds(country)
                    mapOf(
                        "status" to "success",
                        "feeds" to feeds
                    )

                } catch (e: Exception) {
                    mapOf(
                        "status" to "error",
                        "feeds" to emptyList<String>(),
                        "note" to "Failed to load feeds."
                    )
                }
            }


    }
}