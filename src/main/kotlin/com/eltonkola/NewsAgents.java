package com.eltonkola;

import com.google.adk.agents.BaseAgent;

public class NewsAgents {

//    public static BaseAgent ROOT_AGENT = RssSourcesAgent.rssAgent;
//    public static BaseAgent ROOT_AGENT = NewsExtractorAgent.ROOT_AGENT;
    public static BaseAgent ROOT_AGENT = NewsSequentialAgent.coordinator;


}
