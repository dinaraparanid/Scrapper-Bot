package com.paranid5.bot.commands

import com.paranid5.bot.messages.provideTrackLinkMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

private const val TRACK_COMMAND = "/track"
private const val TRACK_DESCRIPTION = "start link's tracking"

data object TrackCommand :
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    override fun matches(command: String): Boolean =
        command == TRACK_COMMAND

    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendTrackMessage(bot, message).await()

    override fun toTgBotCommand(): BotCommand =
        BotCommand(TRACK_COMMAND, TRACK_DESCRIPTION)
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