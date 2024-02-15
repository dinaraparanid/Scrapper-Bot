package com.paranid5.bot.interactor

import com.paranid5.core.entities.user.UserState
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message

interface BotInteractor {
    suspend fun handleCommandAndPatchUserState(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
        userState: UserState
    )
}