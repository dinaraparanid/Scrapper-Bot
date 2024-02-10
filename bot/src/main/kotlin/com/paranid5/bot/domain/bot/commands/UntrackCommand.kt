package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.bot.messages.provideUntrackLinkMessage
import com.paranid5.bot.domain.user.UserState
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class UntrackCommand(override val text: String? = "/untrack") : BotCommand<SendResponse> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
        userState: UserState?
    ): SendResponse = sendUntrackLinkMessage(bot, message, userLinks).await()
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