package com.github.mishannn.articlereader.utils

object UrlValidator {
    val simpleUrlRegex = Regex("^https?://\\S+$", RegexOption.IGNORE_CASE)

    fun isValidUrl(str: String): Boolean {
        return simpleUrlRegex.matches(str)
    }
}