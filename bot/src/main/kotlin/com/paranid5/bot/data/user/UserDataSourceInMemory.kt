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

    override suspend fun patchUser(user: User) {
        if (user !in _usersFlow.value)
            patchUserImpl(user, UserState::NoneState)
    }

    override suspend fun patchUserState(userState: UserState): Unit =
        patchUserImpl(userState.user) { userState }

    private suspend inline fun patchUserImpl(
        user: User,
        userState: (User) -> UserState = UserState::NoneState
    ) {
        _usersFlow.update { it + user }
        _userStatesFlow.update { it + (user.id to userState(user)) }
    }
}