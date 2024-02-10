package com.paranid5.bot.domain.bot.messages

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage

fun registeredMessage(message: Message): SendMessage =
    SendMessage(message.chatId, registeredMessageText(message))

private fun registeredMessageText(message: Message): String =
    "Hello, ${message.from().firstName()} ${message.from().lastName()}!"