package com.paranid5.bot

internal infix fun <T> Collection<T>.matches(iter: Iterable<T>): Boolean =
    all { it in iter } && iter.all { it in this }

internal infix fun <K, V> Map<K, V>.matches(lhs: Map<K, V>): Boolean =
    entries matches lhs.entries