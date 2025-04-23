package com.github.mishannn.articlereader.contentextractor

interface ContentExtractor {
    suspend fun extractContent(message: String): String
}