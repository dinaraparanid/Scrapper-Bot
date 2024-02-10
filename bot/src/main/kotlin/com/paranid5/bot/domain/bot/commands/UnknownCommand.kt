package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.bot.messages.unknownCommandMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class UnknownCommand(override val text: String? = null) : BotCommand<SendResponse> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendUnknownCommandMessage(bot, message).await()
}

private suspend inline fun sendUnknownCommandMessage(
    bot: TelegramBot,
    message: Message,
) = coroutineScope {
    async(Dispatchers.IO) {
        sendUnknownCommandMessageImpl(bot, message)
    }
}

private fun sendUnknownCommandMessageImpl(bot: TelegramBot, message: Message): SendResponse =
    bot.execute(unknownCommandMessage(message))