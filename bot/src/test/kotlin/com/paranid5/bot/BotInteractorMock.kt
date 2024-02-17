package com.paranid5.bot

import com.paranid5.bot.commands.MockCommand
import com.paranid5.bot.commands.TrackLinkCommand
import com.paranid5.bot.commands.UnknownCommand
import com.paranid5.bot.commands.UntrackLinkCommand
import com.paranid5.bot.interactor.BotInteractor
import com.paranid5.com.paranid5.utils.bot.botUser
import com.paranid5.com.paranid5.utils.bot.textOrEmpty
import com.paranid5.core.entities.user.UserState
import com.paranid5.data.link.repository.LinkRepository
import com.paranid5.data.user.user_state_patch.*
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.test.TestScope

class BotInteractorMock(
    linkRepository: LinkRepository,
    helpStatePatch: HelpStatePatchMock,
    listStatePatch: ListStatePatchMock,
    startStatePatch: StartStatePatchMock,
    trackStatePatch: TrackStatePatchMock,
    untrackStatePatch: UntrackStatePatchMock,
    trackLinkStatePatch: TrackLinkStatePatchMock,
    untrackLinkStatePatch: UntrackLinkStatePatchMock,
    scope: TestScope
) : BotInteractor {
    private val stateLessHandlers by lazy {
        listOf(
            MockCommand("/help") to helpStatePatch,
            MockCommand("/list") to listStatePatch,
            MockCommand("/start") to startStatePatch,
            MockCommand("/track") to trackStatePatch,
            MockCommand("/untrack") to untrackStatePatch,
        )
    }

    private val stateFullHandlers by lazy {
        mapOf(
            UserState.TrackSentState::class.simpleName!! to
                    (TrackLinkCommand(linkRepository, scope) to trackLinkStatePatch),
            UserState.UntrackSentState::class.simpleName!! to
                    (UntrackLinkCommand(linkRepository, scope) to untrackLinkStatePatch)
        )
    }

    private val unknownCommand by lazy {
        UnknownCommand
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
        when (val handler = stateFullHandlers[userState::class.simpleName]) {
            null -> unknownCommand.onCommand(bot, message, userLinks)

            else -> {
                val (statusCommand, patcher) = handler
                statusCommand
                    .onCommand(bot, message, userLinks)
                    ?.let { patcher.patchUserState(message.botUser) }
            }
        }
    }

    private suspend inline fun handleStateLessCommands(
        command: String,
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>
    ) {
        when (val handler = stateLessHandlers.find { (cmd, _) -> cmd matches command }) {
            null -> unknownCommand.onCommand(bot, message, userLinks)

            else -> {
                val (cmd, patcher) = handler
                cmd.onCommand(bot, message, userLinks)
                patcher.patchUserState(message.botUser)
            }
        }
    }
}