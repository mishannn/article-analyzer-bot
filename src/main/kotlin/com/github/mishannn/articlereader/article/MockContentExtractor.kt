package com.github.mishannn.articlereader.article

class MockContentExtractor : ContentExtractor {
    override suspend fun extractContent(message: String): String {
        return message
    }
}