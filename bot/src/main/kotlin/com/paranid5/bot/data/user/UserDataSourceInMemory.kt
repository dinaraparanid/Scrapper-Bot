package com.paranid5.bot.data.user

import com.paranid5.bot.domain.user.User
import com.paranid5.bot.domain.user.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.springframework.stereotype.Component

@Component("user_src_memory")
class UserDataSourceInMemory : UserDataSource {
    private val _usersFlow by lazy {
        MutableStateFlow(emptySet<User>())
    }

    override val usersFlow: Flow<Collection<User>> by lazy {
        _usersFlow
    }

    private val _userStatesFlow by lazy {
        MutableStateFlow(mapOf<Long, UserState>())
    }

    override val userStatesFlow: Flow<Map<Long, UserState>> by lazy {
        _userStatesFlow
    }

    override suspend fun patchUser(user: User): Unit =
        _usersFlow.update { it + user }

    override suspend fun patchUserState(userState: UserState): Unit =
        _userStatesFlow.update {
            it + (userState.user.id to userState)
        }
}