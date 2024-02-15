package com.paranid5.bot.messages

import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.request.SendMessage

fun startTrackingMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, startTrackingMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun startTrackingMessageText(link: String): String =
    "Start tracking $link"