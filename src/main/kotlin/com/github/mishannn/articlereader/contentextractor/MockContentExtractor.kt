package com.github.mishannn.articlereader.contentextractor

class MockContentExtractor : ContentExtractor {
    override suspend fun extractContent(message: String): String {
        return message
    }
}