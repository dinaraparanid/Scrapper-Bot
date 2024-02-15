package com.paranid5.com.paranid5.utils.bot

import com.paranid5.core.entities.user.User
import com.pengrad.telegrambot.model.Message

inline val Message.botUser: User
    get() = User(
        id = from().id(),
        chatId = chatId,
        firstName = from().firstName(),
        secondName = from().lastName()
    )

inline val Message.userId: Long
    get() = from().id()

inline val Message.chatId: Long
    get() = chat().id()

inline val Message.textOrEmpty: String
    get() = text() ?: ""