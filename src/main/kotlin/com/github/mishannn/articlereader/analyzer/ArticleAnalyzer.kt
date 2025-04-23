package com.github.mishannn.articlereader.analyzer

interface ArticleAnalyzer {
    suspend fun analyzeArticle(articleText: String): String
}