package com.eltonkola

import com.google.adk.agents.BaseAgent
import com.google.adk.agents.LlmAgent
import com.google.genai.types.GenerateContentConfig


class  NewsImageAgent {

    companion object {

        @JvmField
        val newsImage: BaseAgent = LlmAgent.builder()
            .name("news_to_image_agent")
            .model("gemini-2.0-flash-preview-image-generation")
           // .generateContentConfig(GenerateContentConfig.fromJson("{response_modalities=['IMAGE']}"))
//            .instruction(
//                """
//                Hi, can you create a 3d rendered image of a pig
//                with wings and a top hat flying over a happy
//                futuristic scifi city with lots of greenery?
//                """.trimIndent()
//            )
            .description("Agent to create an image.")
            .build()

    }

}
