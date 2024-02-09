package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

fun onStartCommand(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(registeredMessage(message))

private fun registeredMessage(message: Message): SendMessage =
    SendMessage(message.chatId, registeredMessageText(message))

private fun registeredMessageText(message: Message): String =
    "Hello, ${message.from().firstName()} ${message.from().lastName()}!"