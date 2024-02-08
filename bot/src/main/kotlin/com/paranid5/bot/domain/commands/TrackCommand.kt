package com.paranid5.bot.domain.commands

import com.paranid5.bot.domain.utils.chatId
import com.paranid5.bot.domain.utils.textOrEmpty
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse

fun onTrackCommand(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(provideTrackLinkMessage(message))

private fun provideTrackLinkMessage(message: Message): SendMessage =
    SendMessage(message.chatId, provideTrackLinkMessageText())

private fun provideTrackLinkMessageText(): String =
    """
        Provide the link to track in URL format:
        https://some_link
    """.trimIndent()

fun onTrackLinkCommand(bot: TelegramBot, message: Message): Pair<SendResponse, String> =
    bot.execute(linkReceivedMessage(message)) to message.textOrEmpty

private fun linkReceivedMessage(message: Message): SendMessage =
    SendMessage(message.chatId, linkReceivedMessageText(message))

private fun linkReceivedMessageText(message: Message): String =
    "Start tracking ${message.textOrEmpty}"