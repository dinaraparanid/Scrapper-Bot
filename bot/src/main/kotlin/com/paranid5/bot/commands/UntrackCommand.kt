package com.paranid5.bot.commands

import com.paranid5.bot.messages.provideUntrackLinkMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

private const val UNTRACK_COMMAND = "/untrack"
private const val UNTRACK_DESCRIPTION = "stop link's tracking"

data object UntrackCommand :
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    override fun matches(command: String): Boolean =
        command == UNTRACK_COMMAND

    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendUntrackLinkMessage(bot, message, userLinks).await()

    override fun toTgBotCommand(): BotCommand =
        BotCommand(UNTRACK_COMMAND, UNTRACK_DESCRIPTION)
}

private suspend inline fun sendUntrackLinkMessage(
    bot: TelegramBot,
    message: Message,
    links: List<String>
) = coroutineScope {
    async(Dispatchers.IO) {
        sendUntrackLinkMessageImpl(bot, message, links)
    }
}

private fun sendUntrackLinkMessageImpl(
    bot: TelegramBot,
    message: Message,
    links: List<String>
): SendResponse = bot.execute(provideUntrackLinkMessage(message, links))