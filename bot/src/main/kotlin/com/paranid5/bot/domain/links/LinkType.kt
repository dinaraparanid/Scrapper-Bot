package com.paranid5.bot.domain.links

sealed interface LinkType {
    val link: String

    @JvmInline
    value class GitHubLink(override val link: String) : LinkType

    @JvmInline
    value class StackOverflowLink(override val link: String) : LinkType
}