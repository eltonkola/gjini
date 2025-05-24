//package com.eltonkola.old
//
//import com.eltonkola.news.NewsLoader
//import com.google.adk.agents.BaseAgent
//import com.google.adk.agents.LlmAgent
//import com.google.adk.tools.Annotations
//import com.google.adk.tools.FunctionTool
//import java.text.Normalizer
//import java.time.ZoneId
//import java.time.ZonedDateTime
//import java.time.format.DateTimeFormatter
//import kotlin.jvm.java
//
//
//class  KotToolAgentX {
//
//    companion object {
//
//        @JvmField
//        val ROOT_AGENT: BaseAgent = LlmAgent.builder()
//            .name("kotlin_tool_agent")
//            .model("gemini-2.0-flash")
//            .description("Agent to answer questions about the time, news and weather in a city.")
//            .instruction("You are a helpful agent who can answer user questions about the time, news and weather in a city.")
//            .tools(
//                FunctionTool.create(KotToolAgentX::class.java, "getCurrentTime"),
//                FunctionTool.create(KotToolAgentX::class.java, "getWeather"),
//                FunctionTool.create(KotToolAgentX::class.java, "loadNews")
//            )
//            .build()
//
//
//        @JvmStatic
//        fun getCurrentTime(
//            @Annotations.Schema(description = "City to get time for") city: String
//        ): Map<String, String> {
//            val normalized = Normalizer.normalize(city, Normalizer.Form.NFD)
//                .trim().lowercase().replace("(\\p{IsM}+|\\p{IsP}+)".toRegex(), "").replace("\\s+".toRegex(), "_")
//
//            return ZoneId.getAvailableZoneIds().firstOrNull { it.lowercase().endsWith("/$normalized") }?.let {
//                mapOf(
//                    "status" to "success",
//                    "report" to "The current time in $city is ${
//                        ZonedDateTime.now(ZoneId.of(it)).format(DateTimeFormatter.ofPattern("HH:mm"))
//                    }."
//                )
//            } ?: mapOf(
//                "status" to "error",
//                "report" to "Sorry, I don't have timezone information for $city."
//            )
//        }
//
//        @JvmStatic
//        fun loadNews(
//            @Annotations.Schema(description = "City to get news for") city: String
//        ): Map<String, String> {
//
//
//            return try{
//                val loader = NewsLoader()
//                val newsItems = loader.loadNews()
//
////                newsItems.forEach {
////                    println("📰 ${it.title}")
////                    println("🔗 ${it.link}")
////                    println("📅 ${it.pubDate}")
////                    println("📝 ${it.description?.take(100)}...\n")
////                }
//
//                return mapOf(
//                    "status" to "success",
//                    "report" to "Last News: ${newsItems.firstOrNull()?.title ?: "No news found"}."
//                )
//            }catch (e: Exception) {
//                mapOf(
//                    "status" to "error",
//                    "report" to "Sorry, cant load the news!"
//                )
//            }
//
//        }
//
//        @JvmStatic
//        fun getWeather(
//            @Annotations.Schema(description = "City to get weather for") city: String
//        ): Map<String, String> = if (city.equals("new york", ignoreCase = true)) {
//            mapOf("status" to "success", "report" to "The weather in New York is sunny, 25°C (77°F).")
//        } else {
//            mapOf("status" to "error", "report" to "Weather information for $city is not available.")
//        }
//    }
//}
