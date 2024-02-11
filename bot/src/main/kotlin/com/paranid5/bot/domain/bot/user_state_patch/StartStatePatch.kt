package com.paranid5.bot.domain.bot.user_state_patch

import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.user.UserState
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
data class StartStatePatch(
    @Qualifier("user_src_memory")
    private val userDataSource: UserDataSource
) : UserStatePatch by DefaultUserStatePatch(userDataSource, UserState::StartSentState)

@Component
data class StartStatePatchMock(
    @Qualifier("user_src_mock")
    private val userDataSource: UserDataSource
) : UserStatePatch by DefaultUserStatePatch(userDataSource, UserState::StartSentState)