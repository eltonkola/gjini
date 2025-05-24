package com.eltonkola

import com.google.adk.agents.BaseAgent
import com.google.adk.agents.LlmAgent


class  NewsAudioAgent {

    companion object {

        @JvmField
        val newsAudio: BaseAgent = LlmAgent.builder()
            .name("news_to_mp3_agent")
            .model("gemini-2.5-flash-preview-tts")
            .description("Agent to transform news into a text script suitable for audio.")
            .build()

    }


}
