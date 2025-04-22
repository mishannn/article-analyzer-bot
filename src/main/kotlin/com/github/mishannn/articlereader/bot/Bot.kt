package com.github.mishannn.articlereader.bot

interface Bot {
    suspend fun start(messageHandler: suspend (String) -> String)
}