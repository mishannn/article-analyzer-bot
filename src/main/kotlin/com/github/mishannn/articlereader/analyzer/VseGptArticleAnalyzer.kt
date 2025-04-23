package com.github.mishannn.articlereader.analyzer

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class VseGptArticleAnalyzer(val httpClient: HttpClient, val apiKey: String, val model: String, val prompt: String) :
    ArticleAnalyzer {
    private val logger: Logger = LoggerFactory.getLogger(VseGptArticleAnalyzer::class.java)

    override suspend fun analyzeArticle(articleText: String): String {
        logger.debug("Send article to AI assistant. Model = {}", model)
        val response = httpClient.post("https://api.vsegpt.ru/v1/chat/completions") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
            setBody(
                ChatRequest(
                    model = model,
                    messages = listOf(
                        Message(role = "system", content = prompt),
                        Message(role = "user", content = articleText)
                    )
                )
            )
        }

        if (response.status != HttpStatusCode.OK) {
            val responseBody: String = response.body()
            throw Exception("Сервер вернул ошибку ${response.status}. Детали: $responseBody")
        }

        val data: ChatResponse = response.body()

        return data.choices[0].message.content
    }
}

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<Message>
)

@Serializable
data class ChatResponse(
    val id: String,
    val model: String,

    @SerialName("object")
    val chatResponseObject: String,

    val created: Long,
    val choices: List<Choice>,

    @SerialName("system_fingerprint")
    val systemFingerprint: JsonElement? = null,

    val usage: Usage
)

@Serializable
data class Choice(
    val logprobs: JsonElement? = null,

    @SerialName("finish_reason")
    val finishReason: String,

    @SerialName("native_finish_reason")
    val nativeFinishReason: String,

    val index: Long,
    val message: Message
)

@Serializable
data class Message(
    val role: String,
    val content: String,
    val refusal: JsonElement? = null,
    val reasoning: JsonElement? = null
)

@Serializable
data class Usage(
    @SerialName("prompt_tokens")
    val promptTokens: Long,

    @SerialName("completion_tokens")
    val completionTokens: Long,

    @SerialName("total_tokens")
    val totalTokens: Long,

    @SerialName("prompt_tokens_details")
    val promptTokensDetails: PromptTokensDetails
)

@Serializable
data class PromptTokensDetails(
    @SerialName("cached_tokens")
    val cachedTokens: Long
)