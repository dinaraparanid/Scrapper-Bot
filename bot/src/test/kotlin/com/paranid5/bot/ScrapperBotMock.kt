package com.paranid5.bot

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.data.link.response.LinkResponse
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.bot.ScrapperBot
import com.paranid5.bot.domain.bot.interactor.BotInteractor
import com.paranid5.bot.domain.bot.messages.*
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class ScrapperBotMock(
    private val userDataSource: UserDataSource,
    private val linkRepository: LinkRepository,
    private val botInteractor: BotInteractor,
    private val testScope: TestScope
) : ScrapperBot,
    CoroutineScope by CoroutineScope(UnconfinedTestDispatcher(testScope.testScheduler)) {
    private val messagesFlow by lazy {
        MutableSharedFlow<Message>()
    }

    override val linkResponseFlow: MutableSharedFlow<LinkResponse> by lazy {
        MutableSharedFlow()
    }

    override fun launchBot() {
        val bot = TelegramBot("")
        launch { launchBotEventLoop(bot) }
        launch { launchResponseMonitoring(bot) }
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