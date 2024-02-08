package com.paranid5.bot.data

import com.paranid5.bot.domain.user.UserState
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    val userStatesFlow: Flow<Map<Long, UserState>>
    val usersTrackingFlow: Flow<Map<Long, List<String>>>

    suspend fun patchUserState(userState: UserState)
    suspend fun patchUserTracking(userId: Long, links: List<String>)
}