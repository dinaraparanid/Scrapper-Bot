package com.paranid5.bot.domain.bot.messages

import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.request.SendMessage

fun alreadyTrackingMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, alreadyTrackingMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun alreadyTrackingMessageText(link: String): String =
    "You are already tracking $link"