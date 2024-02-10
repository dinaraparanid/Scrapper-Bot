package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.bot.messages.provideTrackLinkMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class TrackCommand(override val text: String? = "/track") : BotCommand<SendResponse> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendTrackMessage(bot, message).await()
}

private suspend inline fun sendTrackMessage(
    bot: TelegramBot,
    message: Message,
) = coroutineScope {
    async(Dispatchers.IO) {
        sendTrackMessageImpl(bot, message)
    }
}

private fun sendTrackMessageImpl(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(provideTrackLinkMessage(message))