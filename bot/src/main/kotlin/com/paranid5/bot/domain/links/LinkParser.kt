package com.paranid5.bot.domain.links

val GITHUB_REGEX = Regex("https://github\\.com/.*")
val STACK_OVERFLOW_REGEX = Regex("https://stackoverflow\\.com/.*")

fun parseLink(link: String): LinkType? = when {
    link matches GITHUB_REGEX -> LinkType.GitHubLink(link)
    link matches STACK_OVERFLOW_REGEX -> LinkType.StackOverflowLink(link)
    else -> null
}