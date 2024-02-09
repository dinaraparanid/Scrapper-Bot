package com.paranid5.bot.domain.bot

import com.paranid5.bot.configuration.AppConfig
import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.data.link.response.LinkResponse
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.bot.use_cases.*
import com.paranid5.bot.domain.user.botUser
import com.paranid5.bot.domain.utils.textOrEmpty
import com.paranid5.bot.domain.utils.userId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ScrapperBot(
    config: AppConfig,
    @Qualifier("user_src_memory")
    private val userDataSource: UserDataSource,
    @Qualifier("link_rep_memory")
    private val linkRepository: LinkRepository
) : CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private val token = config.telegramToken

    private val messagesFlow by lazy {
        MutableSharedFlow<Message>()
    }

    val linkResponseFlow: MutableSharedFlow<LinkResponse> by lazy {
        MutableSharedFlow()
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

        launch(Dispatchers.IO) { launchBotEventLoop(bot) }
        launch(Dispatchers.IO) { launchResponseMonitoring(bot) }
    }

    private suspend fun launchBotEventLoop(bot: TelegramBot): Unit =
        combine(
            userDataSource.userStatesFlow,
            linkRepository.usersTrackingsFlow,
            messagesFlow
        ) { states, tracking, message ->
            Triple(states, tracking, message)
        }.distinctUntilChanged { (_, _, oldMsg), (_, _, newMsg) ->
            oldMsg == newMsg
        }.collect { (states, tracking, message) ->
            val userId = message.userId
            val userState = states[userId]
            val userLinks = tracking[userId] ?: emptyList()
            val text = message.textOrEmpty

            userDataSource.patchUser(message.botUser)

            when (text) {
                "/start" -> onStartCommand(bot, message, userDataSource)
                "/help" -> onHelpCommand(bot, message, userDataSource)
                "/track" -> onTrackCommand(bot, message, userDataSource)
                "/untrack" -> onUntrackCommand(bot, message, userLinks, userDataSource)
                "/list" -> onListCommand(bot, message, userLinks, userDataSource)
                else -> onTextCommand(bot, message, userState, userDataSource, linkRepository)
            }
        }

    private suspend fun launchResponseMonitoring(bot: TelegramBot): Unit =
        combine(linkResponseFlow, userDataSource.usersFlow) { response, users ->
            response to users
        }.collect { (response, users) ->
            val chatId = users.find { it.id == response.userId }!!.charId

            bot.execute(
                when (response) {
                    is LinkResponse.Invalid -> unsupportedLinkMessage(chatId, response.link)
                    is LinkResponse.TrackResponse.New -> startTrackingMessage(chatId, response.link)
                    is LinkResponse.UntrackResponse.New -> linkNotTrackedMessage(chatId, response.link)
                    is LinkResponse.TrackResponse.Present -> alreadyTrackingMessage(chatId, response.link)
                    is LinkResponse.UntrackResponse.Present -> stopTrackingMessage(chatId, response.link)
                }
            )
        }
}