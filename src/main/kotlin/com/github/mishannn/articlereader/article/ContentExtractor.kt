package com.github.mishannn.articlereader.article

interface ContentExtractor {
    suspend fun extractContent(message: String): String
}