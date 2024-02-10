package com.paranid5.bot.domain.bot.messages

import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.request.SendMessage

fun unsupportedLinkMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, unsupportedLinkMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun unsupportedLinkMessageText(link: String): String =
    "Link $link is not supported"