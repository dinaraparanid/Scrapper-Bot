package com.paranid5.bot.domain.bot.use_cases

import com.paranid5.bot.domain.bot.commands.onTrackCommand
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend inline fun onTrackCommand(
    bot: TelegramBot,
    message: Message,
    userDataSource: UserDataSource
) = coroutineScope {
    launch(Dispatchers.IO) {
        onTrackCommand(bot, message)
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.TrackSentState(message.botUser)
        )
    }
}