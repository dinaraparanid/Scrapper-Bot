package com.paranid5.bot.commands

import com.paranid5.bot.messages.registeredMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class StartCommand(override val text: String? = "/start") : BotCommand<SendResponse> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendStartMessage(bot, message).await()
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