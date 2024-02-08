package com.paranid5.bot.domain

import com.paranid5.bot.configuration.AppConfig
import com.paranid5.bot.data.UserDataSource
import com.paranid5.bot.domain.commands.*
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
import com.paranid5.bot.domain.utils.chatId
import com.paranid5.bot.domain.utils.textOrEmpty
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ScrapperBot(
    config: AppConfig,
    @Qualifier("memory")
    private val userDataSource: UserDataSource
) : CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private val token = config.telegramToken

    private val messagesFlow by lazy {
        MutableSharedFlow<Message>()
    }

    fun launchBot() {
        val bot = TelegramBot(token).apply {
            setUpdatesListener { updates ->
                launch(Dispatchers.IO) {
                    updates.forEach {
                        messagesFlow.emit(it.message())
                    }
                }

                UpdatesListener.CONFIRMED_UPDATES_ALL
            }
        }

        launch(Dispatchers.IO) {
            launchBotEventLoop(bot)
        }
    }

    private suspend fun launchBotEventLoop(bot: TelegramBot): Unit =
        combine(
            userDataSource.userStatesFlow,
            userDataSource.usersTrackingFlow,
            messagesFlow
        ) { states, tracking, message ->
            Triple(states, tracking, message)
        }.collect { (states, tracking, message) ->
            val userId = message.chatId
            val userState = states[userId]
            val userLinks = tracking[userId] ?: emptyList()
            val text = message.textOrEmpty

            when (text) {
                "/start" -> onStartCommand(bot, message, userDataSource)
                "/help" -> onHelpCommand(bot, message, userDataSource)
                "/track" -> onTrackCommand(bot, message, userDataSource)
                "/untrack" -> onUntrackCommand(bot, message, userDataSource)
                "/list" -> onListCommand(bot, message, userLinks, userDataSource)
                else -> onText(bot, message, userState, userLinks, userDataSource)
            }
        }
}

private suspend inline fun onStartCommand(
    bot: TelegramBot,
    message: Message,
    userDataSource: UserDataSource
) = coroutineScope {
    launch(Dispatchers.IO) {
        onStartCommand(bot, message)
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.StartSentState(message.botUser)
        )
    }
}

private suspend inline fun onHelpCommand(
    bot: TelegramBot,
    message: Message,
    userDataSource: UserDataSource
) = coroutineScope {
    launch(Dispatchers.IO) {
        onHelpCommand(bot, message)
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.HelpSentState(message.botUser)
        )
    }
}

private suspend inline fun onTrackCommand(
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

private suspend inline fun onTrackLinkCommand(
    bot: TelegramBot,
    message: Message,
    links: List<String>,
    userDataSource: UserDataSource
) = coroutineScope {
    val (_, link) = withContext(Dispatchers.IO) {
        onTrackLinkCommand(bot, message)
    }

    val user = message.botUser

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.TrackLinkSentState(user)
        )
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserTracking(user.id, links + link)
    }
}

private suspend inline fun onUntrackCommand(
    bot: TelegramBot,
    message: Message,
    userDataSource: UserDataSource
) = coroutineScope {
    launch(Dispatchers.IO) {
        onUntrackCommand(bot, message)
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.UntrackSentState(message.botUser)
        )
    }
}

private suspend inline fun onUntrackLinkCommand(
    bot: TelegramBot,
    message: Message,
    links: List<String>,
    userDataSource: UserDataSource
) = coroutineScope {
    val (_, link) = withContext(Dispatchers.IO) {
        onUntrackLinkCommand(bot, message)
    }

    val user = message.botUser

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.UntrackSentState(user)
        )
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserTracking(user.id, links - link)
    }
}

private suspend inline fun onListCommand(
    bot: TelegramBot,
    message: Message,
    links: List<String>,
    userDataSource: UserDataSource
) = coroutineScope {
    launch(Dispatchers.IO) {
        onListCommand(bot, message, links)
    }

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.LinkListSentState(message.botUser)
        )
    }
}

private suspend inline fun onText(
    bot: TelegramBot,
    message: Message,
    userState: UserState?,
    links: List<String>,
    userDataSource: UserDataSource
) = when (userState) {
    is UserState.TrackSentState ->
        onTrackLinkCommand(bot, message, links, userDataSource)

    is UserState.UntrackSentState ->
        onUntrackLinkCommand(bot, message, links, userDataSource)

    else -> coroutineScope {
        launch(Dispatchers.IO) { onUnknownCommand(bot, message) }
    }
}