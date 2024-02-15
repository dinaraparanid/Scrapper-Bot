package com.paranid5.bot.messages

import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.request.SendMessage

fun stopTrackingMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, stopTrackingMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun stopTrackingMessageText(link: String): String =
    "Stop tracking $link"