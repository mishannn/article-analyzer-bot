package com.github.mishannn.articlereader.bot

class MockBot(val testMessage: String) : Bot {
    override suspend fun start(messageHandler: suspend (String) -> String) {
        println("RECV: $testMessage")

        val returnMessage = messageHandler(testMessage)
        println("SEND: $returnMessage")
    }
}