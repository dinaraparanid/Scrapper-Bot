package com.paranid5.bot.commands

import com.paranid5.bot.messages.helpMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

private const val HELP_COMMAND = "/help"
private const val HELP_DESCRIPTION = "display a list of commands"

data object HelpCommand :
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    override fun matches(command: String): Boolean =
        command == HELP_COMMAND

    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendHelpMessage(bot, message).await()

    override fun toTgBotCommand(): BotCommand =
        BotCommand(HELP_COMMAND, HELP_DESCRIPTION)
}

private suspend inline fun sendHelpMessage(
    bot: TelegramBot,
    message: Message,
) = coroutineScope {
    async(Dispatchers.IO) {
        sendHelpMessageImpl(bot, message)
    }
}

private fun sendHelpMessageImpl(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(helpMessage(message))