package com.paranid5.bot.domain.bot.user_state_patch

import com.paranid5.core.entities.user.User
import com.paranid5.core.entities.user.UserState
import com.paranid5.data.user.UserDataSource
import org.springframework.beans.factory.annotation.Qualifier

class DefaultUserStatePatch(
    @Qualifier("userDataSourceInMemory")
    private val userDataSource: UserDataSource,
    private val userState: (User) -> UserState
) : UserStatePatch {
    override suspend fun patchUserState(user: User): Unit =
        userDataSource.patchUserState(userState(user))
}