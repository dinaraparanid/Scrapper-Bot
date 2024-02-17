package com.paranid5.bot.interactor

import com.paranid5.bot.commands.*
import com.paranid5.com.paranid5.utils.bot.botUser
import com.paranid5.com.paranid5.utils.bot.textOrEmpty
import com.paranid5.core.entities.user.UserState
import com.paranid5.data.link.repository.LinkRepository
import com.paranid5.data.user.user_state_patch.*
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class BotInteractorImpl(
    @Qualifier("linkRepositoryInMemory")
    linkRepository: LinkRepository,
    helpStatePatch: HelpStatePatch,
    listStatePatch: ListStatePatch,
    startStatePatch: StartStatePatch,
    trackLinkStatePatch: TrackLinkStatePatch,
    trackStatePatch: TrackStatePatch,
    untrackStatePatch: UntrackStatePatch,
    untrackLinkStatePatch: UntrackLinkStatePatch
) : TgBotInteractor {
    private val stateLessCommands by lazy {
        listOf(
            HelpCommand to helpStatePatch,
            ListCommand to listStatePatch,
            StartCommand to startStatePatch,
            TrackCommand to trackStatePatch,
            UntrackCommand to untrackStatePatch,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private val stateLessTextCommands by lazy {
        stateLessCommands as List<Pair<BotTextCommand<SendResponse>, UserStatePatch>>
    }

    private val stateFullCommands by lazy {
        mapOf(
            UserState.TrackSentState::class.simpleName!! to (TrackLinkCommand(linkRepository) to trackLinkStatePatch),
            UserState.UntrackSentState::class.simpleName!! to (UntrackLinkCommand(linkRepository) to untrackLinkStatePatch)
        )
    }

    private val unknownCommand by lazy {
        UnknownCommand
    }

    override val botCommands: List<BotCommand>
        get() = (stateLessCommands as List<Pair<ToTgBotCommand, UserStatePatch>>)
            .map { (cmd, patch) -> cmd.toTgBotCommand() }

    override suspend fun handleCommandAndPatchUserState(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
        userState: UserState
    ) {
        val command = findCommand(message.textOrEmpty)
        handleCommandAndPatchUserStateImpl(command, bot, message, userLinks, userState)
    }

    private fun findCommand(text: String): String? =
        stateLessTextCommands
            .find { (cmd, _) -> cmd matches text }
            ?.let { text }

    private suspend inline fun handleCommandAndPatchUserStateImpl(
        command: String?,
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
        userState: UserState
    ): Unit = when (command) {
        null -> handleStateFullCommands(bot, message, userLinks, userState)
        else -> handleStateLessCommands(command, bot, message, userLinks)
    }

    private suspend inline fun handleStateFullCommands(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
        userState: UserState
    ) {
        when (val handler = stateFullCommands[userState::class.simpleName]) {
            null -> unknownCommand.onCommand(bot, message, userLinks)

            else -> {
                val (statusCommand, patcher) = handler
                statusCommand
                    .onCommand(bot, message, userLinks)
                    ?.let { patcher.patchUserState(message.botUser) }
                    ?: statusCommand.onFailure(bot, message)
            }
        }
    }

    private suspend inline fun handleStateLessCommands(
        command: String,
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>
    ) {
        when (val handler = stateLessTextCommands.find { (cmd, _) -> cmd matches command }) {
            null -> unknownCommand.onCommand(bot, message, userLinks)

            else -> {
                val (cmd, patcher) = handler
                cmd.onCommand(bot, message, userLinks)
                patcher.patchUserState(message.botUser)
            }
        }
    }
}