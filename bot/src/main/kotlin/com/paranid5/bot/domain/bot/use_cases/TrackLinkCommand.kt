package com.paranid5.bot.domain.bot.use_cases

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.bot.commands.onTrackLinkCommand
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend inline fun onTrackLinkCommand(
    message: Message,
    userDataSource: UserDataSource,
    linkRepository: LinkRepository
): Unit = coroutineScope {
    withContext(Dispatchers.IO) {
        onTrackLinkCommand(message, linkRepository)
    } ?: return@coroutineScope

    val user = message.botUser

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.TrackLinkSentState(user)
        )
    }
}

fun startTrackingMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, startTrackingMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun startTrackingMessageText(link: String): String =
    "Start tracking $link"

fun alreadyTrackingMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, alreadyTrackingMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun alreadyTrackingMessageText(link: String): String =
    "You are already tracking $link"