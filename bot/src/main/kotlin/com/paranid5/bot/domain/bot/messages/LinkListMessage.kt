package com.paranid5.bot.domain.bot.messages

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage

fun linkListMessage(message: Message, links: List<String>): SendMessage =
    SendMessage(
        message.chatId,
        when {
            links.isEmpty() -> emptyLinkListMessageText()
            else -> nonEmptyLinkListMessageText(links)
        }
    ).linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

fun emptyLinkListMessageText(): String =
    "You are not tracking anything"

private fun nonEmptyLinkListMessageText(links: List<String>): String =
    links
        .mapIndexed { i, link -> "${i + 1}. $link" }
        .joinToString(separator = "\n")