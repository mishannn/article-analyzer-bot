package com.github.mishannn.articlereader.bot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramClient

class TelegramBot(val token: String) : Bot {
    private val telegramClient: TelegramClient = OkHttpTelegramClient(token)
    private val botsApplication = TelegramBotsLongPollingApplication()
    private val logger: Logger = LoggerFactory.getLogger(TelegramBot::class.java)

    private inner class Consumer(private val messageHandler: suspend (String) -> String) :
        LongPollingSingleThreadUpdateConsumer {
        override fun consume(update: Update) {
            logger.debug("Received update. HasMessage = {}", update.hasMessage())

            if (update.hasMessage() && update.message.hasText()) {
                logger.debug("Send loading message")
                val readingMessage = SendMessage.builder()
                    .chatId(update.message.chatId)
                    .replyToMessageId(update.message.messageId)
                    .text("⏳ Читаю статью...")
                    .build()

                val sentMessage = telegramClient.execute(readingMessage)

                CoroutineScope(Dispatchers.Default).async {
                    logger.debug("Execute message handler")
                    val responseText = try {
                        messageHandler(update.message.text)
                    } catch (e: Exception) {
                        "❌ Ошибка: ${e.message ?: "Неизвестная ошибка"}"
                    }

                    logger.debug("Send final message")
                    val finalMessage = EditMessageText.builder()
                        .chatId(update.message.chatId)
                        .messageId(sentMessage.messageId)
                        .text(responseText)
                        .build()

                    telegramClient.execute(finalMessage)
                }
            }
        }
    }

    override suspend fun start(messageHandler: suspend (String) -> String) {
        logger.debug("Register bot consumer")
        botsApplication.registerBot(token, Consumer(messageHandler))
    }
}