package com.eltonkola.news

object RssRepository {
    fun getFeeds(country: String?) : List<String>{
        return if(country != null) {
            rssMap[country] ?: emptyList()
        }else{
            rssMap["usa"]  ?: emptyList()
        }
    }

    //manually added but some from https://github.com/plenaryapp/awesome-rss-feeds

    val rssMap = mapOf(
        "australia" to  listOf(
            "https://www.smh.com.au/rss/feed.xml",
            "https://www.abc.net.au/news/feed/1948/rss.xml",
            "https://www.theage.com.au/rss/feed.xml",
            "https://www.perthnow.com.au/news/feed",
            "https://www.canberratimes.com.au/rss.xml",
            "https://www.brisbanetimes.com.au/rss/feed.xml",
            "http://feeds.feedburner.com/IndependentAustralia",
            "https://www.businessnews.com.au/rssfeed/latest.rss",
            "https://feeds.feedburner.com/com/rCTl",
            "https://www.michaelwest.com.au/feed/",
        ),
        "albania" to  listOf(
            "https://www.lajme.al/feed/",
            "https://abcnews.al/feed/",
            "https://www.news24.al/feed/",
            "https://www.vizionplus.tv/feed/",
            "https://bota.al/feed/",
            "https://javanews.al/feed/",
            "https://www.tiranatimes.com/feed/",
            "https://monitor.al/feed/",
            "https://shekulli.com.al/feed/",
            "https://ata.gov.al/feed/",
        ),
        "italy" to  listOf(
            "https://www.ansa.it/sito/ansait_rss.xml",
            "https://feeds.thelocal.com/rss/it",
            "https://www.diariodelweb.it/rss/home/",
            "https://www.fanpage.it/feed/",
            "https://www.liberoquotidiano.it/rss.xml",
            "https://www.ilmattino.it/?sez=XML&args&p=search&args[box]=Home&limit=20&layout=rss",
            "https://www.milannews.it/rss/",
            "https://www.internazionale.it/sitemaps/rss.xml",
            "https://www.panorama.it/feeds/feed.rss",
            "https://www.theguardian.com/world/italy/rss",
            "https://www.repubblica.it/rss/homepage/rss2.0.xml",
        ),
        "usa" to listOf(
            "https://www.huffpost.com/section/world-news/feed",
            "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml",
            "http://feeds.foxnews.com/foxnews/latest",
            "http://feeds.washingtonpost.com/rss/world",
            "https://feeds.a.dj.com/rss/RSSWorldNews.xml",
            "https://www.latimes.com/world-nation/rss2.0.xml",
            "http://rss.cnn.com/rss/edition.rss",
            "https://news.yahoo.com/rss/mostviewed",
            "https://www.cnbc.com/id/100003114/device/rss/rss.html",
            "https://rss.politico.com/playbook.xml",
        ),

    )

}