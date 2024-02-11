package com.paranid5.bot.domain.bot.commands

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message

interface BotStatusCommand<T> : BotCommand<T> {
    suspend fun onFailure(bot: TelegramBot, message: Message)
}