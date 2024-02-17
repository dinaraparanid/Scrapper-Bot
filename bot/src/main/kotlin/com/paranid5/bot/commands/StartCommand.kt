package com.paranid5.bot.commands

import com.paranid5.bot.messages.registeredMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

private const val START_COMMAND = "/start"
private const val START_DESCRIPTION = "register a user"

data object StartCommand :
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    override fun matches(command: String): Boolean =
        command == START_COMMAND

    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendStartMessage(bot, message).await()

    override fun toTgBotCommand(): BotCommand =
        BotCommand(START_COMMAND, START_DESCRIPTION)
}

private suspend inline fun sendStartMessage(
    bot: TelegramBot,
    message: Message,
) = coroutineScope {
    async(Dispatchers.IO) {
        sendStartMessageImpl(bot, message)
    }
}

private fun sendStartMessageImpl(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(registeredMessage(message))