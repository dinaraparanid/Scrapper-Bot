package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

fun onUnknownCommand(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(unknownCommandMessage(message))

private fun unknownCommandMessage(message: Message): SendMessage =
    SendMessage(message.chatId, unknownCommandMessageText())

private fun unknownCommandMessageText(): String =
    "Unknown command"