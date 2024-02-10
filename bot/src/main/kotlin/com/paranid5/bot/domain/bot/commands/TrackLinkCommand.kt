package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.domain.links.parseLink
import com.paranid5.bot.domain.utils.textOrEmpty
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class TrackLinkCommand(
    private val linkRepository: LinkRepository,
    override val text: String? = null
) : BotCommand<Unit?> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): Unit? = parseAndTrackLink(message, linkRepository).await()
}

private suspend inline fun parseAndTrackLink(
    message: Message,
    linkRepository: LinkRepository
) = coroutineScope {
    async(Dispatchers.IO) {
        parseAndTrackLinkImpl(message, linkRepository)
    }
}

private suspend inline fun parseAndTrackLinkImpl(message: Message, linkRepository: LinkRepository): Unit? =
    parseLink(message.textOrEmpty)?.let {
        linkRepository.trackLink(userId = message.from().id(), link = it)
    }