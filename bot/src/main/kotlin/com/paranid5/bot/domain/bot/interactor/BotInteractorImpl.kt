package com.paranid5.bot.domain.bot.interactor

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.domain.bot.commands.*
import com.paranid5.bot.domain.bot.user_state_patch.*
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
import com.paranid5.bot.domain.utils.textOrEmpty
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component("interactor_impl")
class BotInteractorImpl(
    @Qualifier("link_rep_memory")
    linkRepository: LinkRepository,
    helpStatePatch: HelpStatePatch,
    listStatePatch: ListStatePatch,
    startStatePatch: StartStatePatch,
    trackLinkStatePatch: TrackLinkStatePatch,
    trackStatePatch: TrackStatePatch,
    untrackStatePatch: UntrackStatePatch,
    untrackLinkStatePatch: UntrackLinkStatePatch
) : BotInteractor {
    private val stateLessHandlers by lazy {
        listOf(
            HelpCommand() to helpStatePatch,
            ListCommand() to listStatePatch,
            StartCommand() to startStatePatch,
            TrackCommand() to trackStatePatch,
            UntrackCommand() to untrackStatePatch,
        )
    }

    private val stateFullHandlers by lazy {
        mapOf(
            UserState.TrackSentState::class.simpleName!! to (TrackLinkCommand(linkRepository) to trackLinkStatePatch),
            UserState.UntrackSentState::class.simpleName!! to (UntrackLinkCommand(linkRepository) to untrackLinkStatePatch)
        )
    }

    private val unknownCommand by lazy {
        UnknownCommand()
    }

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
        stateLessHandlers
            .find { (cmd, _) -> cmd.text == text }
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
        when (userState) {
            is UserState.NoneState ->
                unknownCommand.onCommand(bot, message, userLinks)

            else -> {
                val (cmd, patcher) = stateFullHandlers[userState::class.simpleName]!!
                cmd.onCommand(bot, message, userLinks)?.let {
                    patcher.patchUserState(message.botUser)
                }
            }
        }
    }

    private suspend inline fun handleStateLessCommands(
        command: String?,
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>
    ) {
        when (val handler = stateLessHandlers.find { (cmd, _) -> cmd.text == command }) {
            null -> unknownCommand.onCommand(bot, message, userLinks)

            else -> {
                val (cmd, patcher) = handler
                cmd.onCommand(bot, message, userLinks)
                patcher.patchUserState(message.botUser)
            }
        }
    }
}