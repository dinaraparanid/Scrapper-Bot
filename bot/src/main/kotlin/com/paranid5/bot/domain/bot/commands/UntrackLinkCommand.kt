package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.domain.links.parseLink
import com.paranid5.bot.domain.utils.textOrEmpty
import com.pengrad.telegrambot.model.Message

suspend inline fun onUntrackLinkCommand(message: Message, linkRepository: LinkRepository): Unit? =
    parseLink(message.textOrEmpty)?.let {
        linkRepository.untrackLink(message.from().id(), it)
    }