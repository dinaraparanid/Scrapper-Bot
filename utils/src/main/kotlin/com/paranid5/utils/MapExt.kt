package com.paranid5.com.paranid5.utils

infix fun <F, S> Map<F, List<S>>.extend(lhs: Map<F, Iterable<S>>): Map<F, List<S>> =
    toMutableMap().apply {
        lhs.forEach { (key, vals) ->
            val present = get(key) ?: emptyList()
            put(key, present + vals)
        }
    }