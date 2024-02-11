package com.paranid5.bot.domain.bot.user_state_patch

import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.user.UserState
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class UntrackStatePatch(
    @Qualifier("user_src_memory")
    private val userDataSource: UserDataSource
) : UserStatePatch by DefaultUserStatePatch(userDataSource, UserState::UntrackSentState)

@Component
class UntrackStatePatchMock(
    @Qualifier("user_src_mock")
    private val userDataSource: UserDataSource
) : UserStatePatch by DefaultUserStatePatch(userDataSource, UserState::UntrackSentState)