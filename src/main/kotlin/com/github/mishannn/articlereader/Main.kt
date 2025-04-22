package com.github.mishannn.articlereader

import com.github.mishannn.articlereader.article.ArticleAnalyzer
import com.github.mishannn.articlereader.article.ContentExtractor
import com.github.mishannn.articlereader.article.HabrContentExtractor
import com.github.mishannn.articlereader.article.VseGptArticleAnalyzer
import com.github.mishannn.articlereader.bot.Bot
import com.github.mishannn.articlereader.bot.TelegramBot
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    val logger: Logger = LoggerFactory.getLogger("main")

    logger.debug("Load config and init dependencies")
    val config = Config.load()

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
    val bot: Bot = TelegramBot(config.telegramToken)
    val contentExtractor: ContentExtractor = HabrContentExtractor(httpClient)
    val articleAnalyzer: ArticleAnalyzer = VseGptArticleAnalyzer(
        httpClient,
        config.aiAssistantToken,
        config.aiAssistantModel,
        config.aiAssistantPrompt
    )

    logger.debug("Start bot")
    runBlocking {
        bot.start { message ->
            logger.debug("Received message: {}", message)

            logger.debug("Extract article content")
            val articleContent = contentExtractor.extractContent(message)

            logger.debug("Analyze article")
            articleAnalyzer.analyzeArticle(articleContent)
        }
    }
}
