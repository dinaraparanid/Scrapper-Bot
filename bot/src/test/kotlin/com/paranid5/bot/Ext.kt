package com.paranid5.bot

import com.paranid5.bot.data.link.response.LinkResponse

internal infix fun <T> Collection<T>.matches(iter: Iterable<T>): Boolean =
    all { it in iter } && iter.all { it in this }

internal infix fun <K, V> Map<K, V>.matches(lhs: Map<K, V>): Boolean =
    entries matches lhs.entries

internal infix fun LinkResponse.matches(lhs: LinkResponse): Boolean =
    this::class == lhs::class && userId == lhs.userId && link == lhs.link