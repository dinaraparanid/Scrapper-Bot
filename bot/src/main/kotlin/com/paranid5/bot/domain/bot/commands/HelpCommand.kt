package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

fun onHelpCommand(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(helpMessage(message))

private fun helpMessage(message: Message): SendMessage =
    SendMessage(message.chatId, helpMessageText())
        .parseMode(ParseMode.Markdown)

private fun helpMessageText(): String =
    """
        *start* - register a user
        *help* - display a list of commands
        *track* - start link's tracking
        *untrack* - stop link's tracking
        *list* - display a list of tracking links
    """.trimIndent()