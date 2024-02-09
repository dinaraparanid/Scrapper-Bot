package com.paranid5.bot.domain.bot

import com.pengrad.telegrambot.response.SendResponse

data class ResponseWithUrl(
    val response: SendResponse,
    val url: String?
)