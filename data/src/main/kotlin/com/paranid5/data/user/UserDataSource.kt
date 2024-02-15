package com.paranid5.data.user

import com.paranid5.core.entities.user.User
import com.paranid5.core.entities.user.UserState
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    val usersFlow: Flow<Collection<User>>
    suspend fun patchUser(user: User)

    val userStatesFlow: Flow<Map<Long, UserState>>
    suspend fun patchUserState(userState: UserState)
}