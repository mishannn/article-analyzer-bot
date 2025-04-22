package com.github.mishannn.articlereader

import java.util.Properties

data class Config(
    val telegramToken: String,
    val aiAssistantToken: String,
    val aiAssistantModel: String,
    val aiAssistantPrompt: String
) {
    companion object {
        fun load(): Config {
            return Config(
                telegramToken = get("TELEGRAM_TOKEN"),
                aiAssistantToken = get("AI_ASSISTANT_TOKEN"),
                aiAssistantModel = get("AI_ASSISTANT_MODEL"),
                aiAssistantPrompt = get("AI_ASSISTANT_PROMPT"),
            )
        }

        private fun get(key: String): String {
            return System.getenv(key)
                ?: throw IllegalStateException("Missing config value for $key")
        }
    }
}