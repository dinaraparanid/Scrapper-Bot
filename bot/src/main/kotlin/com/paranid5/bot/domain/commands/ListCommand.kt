package com.paranid5.bot.domain.commands

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

fun onListCommand(bot: TelegramBot, message: Message, links: List<String>): SendResponse =
    bot.execute(linkListMessage(message, links))

private fun linkListMessage(message: Message, links: List<String>): SendMessage =
    SendMessage(
        message.chatId,
        when {
            links.isEmpty() -> emptyLinkListMessageText()
            else -> nonEmptyLinkListMessageText(links)
        }
    )

private fun emptyLinkListMessageText(): String =
    "You are not tracking anything"

private fun nonEmptyLinkListMessageText(links: List<String>): String =
    links
        .mapIndexed { i, link -> "${i + 1}. $link" }
        .joinToString(separator = "\n")