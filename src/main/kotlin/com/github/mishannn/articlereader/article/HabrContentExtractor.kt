package com.github.mishannn.articlereader.article

import com.github.mishannn.articlereader.bot.TelegramBot
import com.github.mishannn.articlereader.utils.UrlValidator
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HabrContentExtractor(
    private val httpClient: HttpClient
) : ContentExtractor {
    private val logger: Logger = LoggerFactory.getLogger(HabrContentExtractor::class.java)

    override suspend fun extractContent(message: String): String {
        val url = message.trim()

        require(UrlValidator.isValidUrl(url)) {
            "Сообщение должно содержать только ссылку на статью на Habr!"
        }

        logger.debug("Get article from Habr")
        val response = httpClient.get(url) {
            headers {
                append(HttpHeaders.ContentType, "text/html")
            }
        }

        check(response.status == HttpStatusCode.OK) {
            "Не удалось получить текст статьи. Habr вернул ошибку: ${response.status}"
        }

        logger.debug("Parse article")
        val document = Jsoup.parse(response.bodyAsText())

        return document.selectFirst(".tm-article-body")?.text()
            ?: throw IllegalStateException("Не удалось получить текст статьи. Статья не найдена в ответе Habr.")
    }
}