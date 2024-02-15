package com.paranid5.bot.messages

import com.paranid5.com.paranid5.utils.bot.chatId
import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage

fun provideTrackLinkMessage(message: Message): SendMessage =
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