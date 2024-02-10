package com.paranid5.bot.domain.bot

import com.paranid5.bot.configuration.AppConfig
import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.data.link.response.LinkResponse
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.bot.messages.*
import com.paranid5.bot.domain.bot.interactor.BotInteractor
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
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

@Component("bot_impl")
class ScrapperBotImpl(
    config: AppConfig,
    @Qualifier("user_src_memory")
    private val userDataSource: UserDataSource,
    @Qualifier("link_rep_memory")
    private val linkRepository: LinkRepository,
    @Qualifier("interactor_impl")
    private val botInteractor: BotInteractor
) : ScrapperBot,
    CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private val token = config.telegramToken

    private val messagesFlow by lazy {
        MutableSharedFlow<Message>()
    }

    override val linkResponseFlow: MutableSharedFlow<LinkResponse> by lazy {
        MutableSharedFlow()
    }

    override fun launchBot() {
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
            val user = message.botUser
            val userState = states[user.id] ?: UserState.NoneState(user)
            val userLinks = tracking[user.id] ?: emptyList()

            userDataSource.patchUser(message.botUser)
            botInteractor.handleCommandAndPatchUserState(bot, message, userLinks, userState)
        }

    private suspend fun launchResponseMonitoring(bot: TelegramBot): Unit =
        combine(linkResponseFlow, userDataSource.usersFlow) { response, users ->
            response to users
        }.distinctUntilChanged { (oldResponse, _), (newResponse, _) ->
            oldResponse == newResponse
        }.collect { (response, users) ->
            val chatId = users.find { it.id == response.userId }!!.chatId

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