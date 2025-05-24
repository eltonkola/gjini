package com.eltonkola.news

import org.w3c.dom.Element
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

data class News(
    val title: String,
    val link: String,
    val description: String?,
    val pubDate: String?
){
    val summary: String
        get() =  title + description ?: ""
}

fun main() {

    val loader = NewsLoader()
    val newsItems = loader.loadNews("https://www.lajme.al/feed/")

    newsItems.forEach {
        println("📰 ${it.title}")
        println("🔗 ${it.link}")
        println("📅 ${it.pubDate}")
        println("📝 ${it.description?.take(100)}...\n")
    }

}

class NewsLoader {


    fun loadNews(feedUrl: String): List<News> {
        val newsList = mutableListOf<News>()

        try {
            val xml = URL(feedUrl).openStream()
            val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc = docBuilder.parse(xml)
            doc.documentElement.normalize()

            val items = doc.getElementsByTagName("item")

            for (i in 0 until items.length) {
                val element = items.item(i) as? Element ?: continue

                val title = element.getElementsByTagName("title").item(0)?.textContent?.trim().orEmpty()
                val link = element.getElementsByTagName("link").item(0)?.textContent?.trim().orEmpty()
                val description = element.getElementsByTagName("description").item(0)?.textContent?.trim()
                val pubDate = element.getElementsByTagName("pubDate").item(0)?.textContent?.trim()

                newsList.add(News(title, link, description, pubDate))
            }

            xml.close()

        } catch (e: Exception) {
            println("❌ Error loading RSS feed: ${e.message}")
        }

        return newsList
    }

    private val cache = mutableMapOf<String, Pair<Long, List<News>>>()
    private val cacheDurationMs = 5 * 60 * 1000 // 5 minutes in milliseconds


    fun loadFromFeeds(feeds: List<String>): List<List<News>> {
        val now = System.currentTimeMillis()
        return feeds.map { feedUrl ->
            val cached = cache[feedUrl]
            if (cached != null && now - cached.first < cacheDurationMs) {
                // Return cached news
                cached.second
            } else {
                // Load fresh news and update cache
                val freshNews = loadNews(feedUrl)
                cache[feedUrl] = now to freshNews
                freshNews
            }
        }
    }
}
