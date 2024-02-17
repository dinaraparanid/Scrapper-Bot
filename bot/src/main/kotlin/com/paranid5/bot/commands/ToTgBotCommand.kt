package com.paranid5.bot.commands

import com.pengrad.telegrambot.model.BotCommand

interface ToTgBotCommand {
    fun toTgBotCommand(): BotCommand
}