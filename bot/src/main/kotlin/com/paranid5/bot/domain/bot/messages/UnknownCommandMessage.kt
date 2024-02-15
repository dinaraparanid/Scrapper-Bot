package com.paranid5.bot.domain.bot.messages

import com.paranid5.com.paranid5.utils.bot.chatId
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage

fun unknownCommandMessage(message: Message): SendMessage =
    SendMessage(message.chatId, unknownCommandMessageText())

private fun unknownCommandMessageText(): String =
    "Unknown command"