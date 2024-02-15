package com.paranid5.bot.messages

import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.request.SendMessage

fun linkNotTrackedMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, linkNotTrackedMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun linkNotTrackedMessageText(link: String): String =
    "You are not tracking $link"