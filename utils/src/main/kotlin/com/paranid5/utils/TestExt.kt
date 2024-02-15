package com.paranid5.utils

import com.paranid5.core.entities.link.LinkResponse

infix fun <T> Collection<T>.matches(iter: Iterable<T>): Boolean =
    all { it in iter } && iter.all { it in this }

infix fun <K, V> Map<K, V>.matches(lhs: Map<K, V>): Boolean =
    entries matches lhs.entries

infix fun LinkResponse.matches(lhs: LinkResponse): Boolean =
    this::class == lhs::class && userId == lhs.userId && link == lhs.link