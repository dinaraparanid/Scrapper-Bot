package com.paranid5.bot.domain.commands

import com.paranid5.bot.domain.utils.chatId
import com.paranid5.bot.domain.utils.textOrEmpty
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

val SUPPORTED_URL_REGEX = listOf(
    Regex("https://github\\.com/.*"),
    Regex("https://stackoverflow\\.com/.*")
)

data class ResponseWithUrl(
    val response: SendResponse,
    val url: String?
)

fun onTrackCommand(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(provideTrackLinkMessage(message))

private fun provideTrackLinkMessage(message: Message): SendMessage =
    SendMessage(message.chatId, provideTrackLinkMessageText())
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun provideTrackLinkMessageText(): String =
    """
        Provide the link to track in URL format:
        https://some_link

        Supported sites:
        1. https://github.com
        2. https://stackoverflow.com
    """.trimIndent()

fun onTrackLinkCommand(bot: TelegramBot, message: Message, links: List<String>): ResponseWithUrl {
    val url = message.textOrEmpty

    return when {
        SUPPORTED_URL_REGEX.none(url::matches) -> ResponseWithUrl(
            response = bot.execute(unsupportedLinkMessage(message)),
            url = null
        )

        url in links -> ResponseWithUrl(
            response = bot.execute(alreadyTrackingMessage(message)),
            url = null
        )

        else -> ResponseWithUrl(
            response = bot.execute(startTrackingMessage(message)),
            url = url
        )
    }
}

private fun startTrackingMessage(message: Message): SendMessage =
    SendMessage(message.chatId, startTrackingMessageText(message))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun startTrackingMessageText(message: Message): String =
    "Start tracking ${message.textOrEmpty}"

fun unsupportedLinkMessage(message: Message): SendMessage =
    SendMessage(message.chatId, unsupportedLinkMessageText(message))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun unsupportedLinkMessageText(message: Message): String =
    "Link ${message.textOrEmpty} is not supported"

fun alreadyTrackingMessage(message: Message): SendMessage =
    SendMessage(message.chatId, alreadyTrackingMessageText(message))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun alreadyTrackingMessageText(message: Message): String =
    "You are already tracking ${message.textOrEmpty}"