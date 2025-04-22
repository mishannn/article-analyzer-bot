package com.github.mishannn.articlereader.article

class MockArticleAnalyzer : ArticleAnalyzer {
    override suspend fun analyzeArticle(articleText: String): String {
        return "ANALYZED: $articleText"
    }
}