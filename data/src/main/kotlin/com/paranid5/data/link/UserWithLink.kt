package com.paranid5.data.link

data class UserWithLink(
    val userId: Long,
    val link: String,
    private val id: Long = System.currentTimeMillis()
)