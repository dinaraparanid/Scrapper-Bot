package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.bot.messages.helpMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class HelpCommand(override val text: String? = "/help") : BotCommand<SendResponse> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendHelpMessage(bot, message).await()
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