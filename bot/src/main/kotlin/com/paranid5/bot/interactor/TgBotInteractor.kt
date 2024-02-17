package com.paranid5.bot.interactor

import com.pengrad.telegrambot.model.BotCommand

interface TgBotInteractor : BotInteractor {
    val botCommands: List<BotCommand>
}