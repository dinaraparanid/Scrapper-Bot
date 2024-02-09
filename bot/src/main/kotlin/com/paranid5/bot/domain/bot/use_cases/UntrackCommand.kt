package com.paranid5.bot.domain.bot.use_cases

import com.paranid5.bot.domain.bot.commands.onUntrackCommand
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend inline fun onUntrackCommand(
    bot: TelegramBot,
    message: Message,
    links: List<String>,
    userDataSource: UserDataSource
) = coroutineScope {
    launch(Dispatchers.IO) {
        onUntrackCommand(bot, message, links)
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.UntrackSentState(message.botUser)
        )
    }
}