package com.paranid5.bot.domain.utils

import com.pengrad.telegrambot.model.Message

inline val Message.userId: Long
    get() = from().id()

inline val Message.chatId: Long
    get() = chat().id()

inline val Message.textOrEmpty: String
    get() = text() ?: ""