package com.paranid5.bot.domain.bot.use_cases

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.bot.commands.onUntrackLinkCommand
import com.paranid5.bot.domain.user.UserState
import com.paranid5.bot.domain.user.botUser
import com.pengrad.telegrambot.model.LinkPreviewOptions
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.SendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend inline fun onUntrackLinkCommand(
    message: Message,
    userDataSource: UserDataSource,
    linkRepository: LinkRepository
): Unit = coroutineScope {
    withContext(Dispatchers.IO) {
        onUntrackLinkCommand(message, linkRepository)
    } ?: return@coroutineScope

    val user = message.botUser

    launch(Dispatchers.IO) {
        userDataSource.patchUserState(
            UserState.UntrackSentState(user)
        )
    }
}

fun stopTrackingMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, stopTrackingMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun stopTrackingMessageText(link: String): String =
    "Stop tracking $link"

fun linkNotTrackedMessage(chatId: Long, link: String): SendMessage =
    SendMessage(chatId, linkNotTrackedMessageText(link))
        .linkPreviewOptions(LinkPreviewOptions().isDisabled(true))

private fun linkNotTrackedMessageText(link: String): String =
    "You are not tracking $link"