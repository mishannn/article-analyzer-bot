package com.github.mishannn.articlereader.analyzer

class MockArticleAnalyzer : ArticleAnalyzer {
    override suspend fun analyzeArticle(articleText: String): String {
        return "ANALYZED: $articleText"
    }
}