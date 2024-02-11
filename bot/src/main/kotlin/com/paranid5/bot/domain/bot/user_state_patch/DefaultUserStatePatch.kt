package com.paranid5.bot.domain.bot.user_state_patch

import com.paranid5.bot.data.user.UserDataSource
import com.paranid5.bot.domain.user.User
import com.paranid5.bot.domain.user.UserState
import org.springframework.beans.factory.annotation.Qualifier

class DefaultUserStatePatch(
    @Qualifier("user_src_memory")
    private val userDataSource: UserDataSource,
    private val userState: (User) -> UserState
) : UserStatePatch {
    override suspend fun patchUserState(user: User): Unit =
        userDataSource.patchUserState(userState(user))
}