package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.utils.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

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