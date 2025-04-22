package com.github.mishannn.articlereader.article

interface ArticleAnalyzer {
    suspend fun analyzeArticle(articleText: String): String
}