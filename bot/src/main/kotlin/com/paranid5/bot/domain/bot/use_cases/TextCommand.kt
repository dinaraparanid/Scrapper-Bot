package com.paranid5.bot.domain.bot.use_cases

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.bot.commands.onUnknownCommand
import com.paranid5.bot.domain.user.UserState
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend inline fun onTextCommand(
    bot: TelegramBot,
    message: Message,
    userState: UserState?,
    userDataSource: UserDataSource,
    linkRepository: LinkRepository
) {
    when (userState) {
        is UserState.TrackSentState ->
            onTrackLinkCommand(message, userDataSource, linkRepository)

        is UserState.UntrackSentState ->
            onUntrackLinkCommand(message, userDataSource, linkRepository)

        else -> coroutineScope {
            launch(Dispatchers.IO) { onUnknownCommand(bot, message) }
        }
    }
}