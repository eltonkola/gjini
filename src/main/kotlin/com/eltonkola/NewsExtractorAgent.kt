package com.eltonkola

import com.eltonkola.news.NewsLoader
import com.google.adk.agents.BaseAgent
import com.google.adk.agents.LlmAgent
import com.google.adk.tools.Annotations
import com.google.adk.tools.FunctionTool
import kotlin.jvm.java


class  NewsExtractorAgent {

    companion object {

        @JvmField
        val newsExtractor: BaseAgent = LlmAgent.builder()
            .name("rss_extractor_agent")
            .model("gemini-2.0-flash")
            .description("Agent to read news from rss sources.")
            .instruction(
                """    
        You are a helpful agent that reads news from rss sources using the getNews tool.
        You must summarize the most relevant news on the requested topic using the titles and descriptions returned.
        Present the summary as a 10 minute read.
    """.trimIndent()
            )
            .tools(
                FunctionTool.create(NewsExtractorAgent::class.java, "getNews")
            )
            .build()



        @JvmStatic
        fun getNews(
            @Annotations.Schema(description = "List of RSS feed URLs") feeds: List<String>
        ): Map<String, Any> {
            return try {
                return try {
                    val loader = NewsLoader()
                    val newsItems = loader.loadFromFeeds(feeds).map { it.take(5) }.flatten()

                    val summarizedItems = newsItems.map {
                        mapOf(
                            "title" to it.title,
                            "description" to it.description
                        )
                    }

                    mapOf(
                        "status" to "success",
                        "news" to summarizedItems
                    )
                } catch (e: Exception) {
                    mapOf(
                        "status" to "error",
                        "note" to "Could not load or summarize news from the feeds."
                    )
                }

            } catch (e: Exception) {
                mapOf(
                    "status" to "error",
                    "news" to emptyList<String>(),
                    "note" to "Failed to load news."
                )
            }
        }
    }

}
