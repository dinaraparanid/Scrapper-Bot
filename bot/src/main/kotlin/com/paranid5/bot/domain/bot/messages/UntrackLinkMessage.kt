package com.paranid5.bot.domain.bot.messages

import com.paranid5.com.paranid5.utils.bot.chatId
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage

fun provideUntrackLinkMessage(message: Message, links: List<String>): SendMessage =
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