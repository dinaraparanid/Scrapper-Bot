package com.paranid5.bot.domain.user

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.model.Message

data class User(
    val id: Long,
    val chatId: Long,
    val firstName: String,
    val secondName: String
)

inline val Message.botUser: User
    get() = User(
        id = from().id(),
        chatId = chatId,
        firstName = from().firstName(),
        secondName = from().lastName()
    )
