package com.paranid5.bot.domain.commands

import com.paranid5.bot.domain.utils.chatId
import com.paranid5.bot.domain.utils.textOrEmpty
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

fun onUntrackCommand(bot: TelegramBot, message: Message, links: List<String>): SendResponse =
    bot.execute(provideUntrackLinkMessage(message, links))

private fun provideUntrackLinkMessage(message: Message, links: List<String>): SendMessage =
    when {
        links.isEmpty() -> SendMessage(message.chatId, emptyLinkListMessageText())

        else -> SendMessage(message.chatId, provideUntrackLinkMessageText())
            .replyMarkup(linksKeyboard(links))
    }

private fun provideUntrackLinkMessageText(): String =
    "Select the link to untrack"

private fun linksKeyboard(links: List<String>): ReplyKeyboardMarkup =
    ReplyKeyboardMarkup(*links.map { arrayOf(it) }.toTypedArray())
        .resizeKeyboard(true)
        .oneTimeKeyboard(true)

fun onUntrackLinkCommand(bot: TelegramBot, message: Message, links: List<String>): ResponseWithUrl {
    val url = message.textOrEmpty

    return when {
        SUPPORTED_URL_REGEX.none(url::matches) -> ResponseWithUrl(
            response = bot.execute(unsupportedLinkMessage(message)),
            url = null
        )

        url !in links -> ResponseWithUrl(
            response = bot.execute(linkNotTrackedMessage(message)),
            url = null
        )

        else -> ResponseWithUrl(
            response = bot.execute(linkReceivedMessage(message)),
            url = message.textOrEmpty
        )
    }
}

private fun linkReceivedMessage(message: Message): SendMessage =
    SendMessage(message.chatId, linkReceivedMessageText(message))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun linkReceivedMessageText(message: Message): String =
    "Stop tracking ${message.textOrEmpty}"

private fun linkNotTrackedMessage(message: Message): SendMessage =
    SendMessage(message.chatId, linkNotTrackedMessageText(message))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun linkNotTrackedMessageText(message: Message): String =
    "You are not tracking ${message.textOrEmpty}"