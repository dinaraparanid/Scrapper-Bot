package com.paranid5.bot.commands

sealed interface BotTextCommand<T> : BotCommand<T> {
    infix fun matches(command: String): Boolean
}