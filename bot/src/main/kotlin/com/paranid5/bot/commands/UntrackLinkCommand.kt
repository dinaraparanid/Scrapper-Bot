package com.paranid5.bot.commands

import com.paranid5.bot.messages.unsupportedLinkMessage
import com.paranid5.com.paranid5.utils.bot.chatId
import com.paranid5.com.paranid5.utils.bot.textOrEmpty
import com.paranid5.core.entities.link.parseLink
import com.paranid5.data.link.repository.LinkRepository
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.*

data class UntrackLinkCommand(
    private val linkRepository: LinkRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : BotStatusCommand<Unit?> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): Unit? = parseAndUntrackLink(message, linkRepository, scope).await()

    override suspend fun onFailure(bot: TelegramBot, message: Message): Unit = coroutineScope {
        launch(scope.coroutineContext) {
            bot.execute(
                unsupportedLinkMessage(
                    chatId = message.chatId,
                    link = message.textOrEmpty
                )
            )
        }
    }
}

private suspend inline fun parseAndUntrackLink(
    message: Message,
    linkRepository: LinkRepository,
    scope: CoroutineScope
) = coroutineScope {
    async(scope.coroutineContext) {
        parseAndUntrackLinkImpl(message, linkRepository)
    }
}

private suspend inline fun parseAndUntrackLinkImpl(message: Message, linkRepository: LinkRepository): Unit? =
    parseLink(message.textOrEmpty)?.let {
        linkRepository.untrackLink(message.from().id(), it)
    }