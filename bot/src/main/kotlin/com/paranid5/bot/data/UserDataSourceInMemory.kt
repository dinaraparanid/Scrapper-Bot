package com.paranid5.bot.data

import com.paranid5.bot.domain.user.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.springframework.stereotype.Component

@Component(value = "memory")
class UserDataSourceInMemory : UserDataSource {
    private val _userStatesFlow by lazy {
        MutableStateFlow(hashMapOf<Long, UserState>())
    }

    override val userStatesFlow: Flow<Map<Long, UserState>> by lazy {
        _userStatesFlow
    }

    private val _userTrackingFlow by lazy {
        MutableStateFlow(hashMapOf<Long, List<String>>())
    }

    override val usersTrackingFlow: Flow<Map<Long, List<String>>> by lazy {
        _userTrackingFlow
    }

    override suspend fun patchUserState(userState: UserState): Unit =
        _userStatesFlow.update {
            it[userState.user.id] = userState
            it
        }

    override suspend fun patchUserTracking(userId: Long, links: List<String>): Unit =
        _userTrackingFlow.update {
            it[userId] = links
            it
        }
}