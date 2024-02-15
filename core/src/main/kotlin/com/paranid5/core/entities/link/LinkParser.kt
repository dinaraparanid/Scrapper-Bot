package com.paranid5.core.entities.link

private val GITHUB_REGEX = Regex("https://github\\.com/.*")
private val STACK_OVERFLOW_REGEX = Regex("https://stackoverflow\\.com/.*")

fun parseLink(link: String): LinkType? = when {
    link matches GITHUB_REGEX -> LinkType.GitHubLink(link)
    link matches STACK_OVERFLOW_REGEX -> LinkType.StackOverflowLink(link)
    else -> null
}