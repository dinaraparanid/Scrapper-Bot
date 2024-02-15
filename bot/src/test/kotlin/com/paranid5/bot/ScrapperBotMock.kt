package com.paranid5.bot

import com.paranid5.bot.interactor.BotInteractor
import com.paranid5.com.paranid5.utils.bot.botUser
import com.paranid5.core.bot.ScrapperBot
import com.paranid5.core.entities.link.LinkResponse
import com.paranid5.core.entities.user.User
import com.paranid5.core.entities.user.UserState
import com.paranid5.data.link.repository.LinkRepository
import com.paranid5.data.user.UserDataSource
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.User as TgUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.io.Serial
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class ScrapperBotMock(
    private val userDataSource: UserDataSource,
    private val linkRepository: LinkRepository,
    private val botInteractor: BotInteractor,
    private val testScope: TestScope
) : ScrapperBot {
    private val messagesFlow by lazy {
        MutableSharedFlow<Message>()
    }

    private val _linkResponseFlow by lazy {
        MutableSharedFlow<LinkResponse>()
    }

    val linkResponseFlow by lazy {
        _linkResponseFlow.asSharedFlow()
    }

    override fun launchBot() {
        testScope.backgroundScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
            launchBotEventLoop(TelegramBot(""))
        }
    }

    override suspend fun acquireResponse(response: LinkResponse): Unit =
        _linkResponseFlow.emit(response)

    suspend fun sendStartMessage(user: User): Unit = sendMessage(user, "/start")
    suspend fun sendHelpMessage(user: User): Unit = sendMessage(user, "/help")
    suspend fun sendTrackMessage(user: User): Unit = sendMessage(user, "/track")
    suspend fun sendUntrackMessage(user: User): Unit = sendMessage(user, "/untrack")
    suspend fun sendListMessage(user: User): Unit = sendMessage(user, "/list")
    suspend fun sendTextMessage(user: User, text: String): Unit = sendMessage(user, text)

    private suspend fun sendMessage(
        user: User,
        message: String,
        messageId: Int = Random.nextInt()
    ) = messagesFlow.emit(
        object : Message() {
            @Serial
            private val serialVersionUID: Long = -5235462941514739848L

            override fun messageId(): Int = messageId

            override fun text(): String = message

            override fun from(): TgUser = object : TgUser(user.id) {
                @Serial
                private val serialVersionUID: Long = 2544479605889672743L

                override fun firstName(): String =
                    user.firstName

                override fun lastName(): String =
                    user.secondName
            }

            override fun chat(): Chat = object : Chat() {
                @Serial
                private val serialVersionUID: Long = -4449400557775734231L

                override fun id(): Long = user.chatId
            }
        }
    )

    private suspend fun launchBotEventLoop(bot: TelegramBot): Unit =
        combine(
            userDataSource.userStatesFlow,
            linkRepository.usersTrackingsFlow,
            messagesFlow
        ) { states, tracking, message ->
            Triple(states, tracking, message)
        }.distinctUntilChanged { (_, _, oldMsg), (_, _, newMsg) ->
            oldMsg.messageId() == newMsg.messageId()
        }.collect { (states, tracking, message) ->
            val user = message.botUser
            val userState = states[user.id] ?: UserState.NoneState(user)
            val userLinks = tracking[user.id] ?: emptyList()

            userDataSource.patchUser(user)
            botInteractor.handleCommandAndPatchUserState(bot, message, userLinks, userState)
        }
}