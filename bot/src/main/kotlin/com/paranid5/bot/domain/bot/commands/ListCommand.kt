package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.bot.messages.linkListMessage
import com.paranid5.bot.domain.user.UserState
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class ListCommand(override val text: String? = "/list") : BotCommand<SendResponse> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
        userState: UserState?
    ): SendResponse = sendListMessage(bot, message, userLinks).await()
}

private suspend inline fun sendListMessage(
    bot: TelegramBot,
    message: Message,
    links: List<String>,
) = coroutineScope {
    async(Dispatchers.IO) {
        sendListMessageImpl(bot, message, links)
    }
}

private fun sendListMessageImpl(bot: TelegramBot, message: Message, links: List<String>): SendResponse =
    bot.execute(linkListMessage(message, links))