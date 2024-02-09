package com.paranid5.bot.data.user

import com.paranid5.bot.domain.user.User
import com.paranid5.bot.domain.user.UserState
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    val usersFlow: Flow<Collection<User>>
    suspend fun patchUser(user: User)

    val userStatesFlow: Flow<Map<Long, UserState>>
    suspend fun patchUserState(userState: UserState)
}