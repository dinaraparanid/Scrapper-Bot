package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.user.UserState
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message

sealed interface BotCommand<T> {
    val text: String?

    suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
        userState: UserState?,
    ): T
}