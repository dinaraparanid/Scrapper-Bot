package com.paranid5.bot.domain.bot

import com.paranid5.bot.data.link.response.LinkResponse
import kotlinx.coroutines.flow.MutableSharedFlow

interface ScrapperBot {
    val linkResponseFlow: MutableSharedFlow<LinkResponse>

    fun launchBot()
}