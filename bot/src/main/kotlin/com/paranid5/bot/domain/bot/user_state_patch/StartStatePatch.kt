package com.paranid5.bot.domain.bot.user_state_patch

import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.user.UserState
import org.springframework.stereotype.Component

@Component
data class StartStatePatch(private val userDataSource: UserDataSource) :
    UserStatePatch by DefaultUserStatePatch(userDataSource, UserState::StartSentState)