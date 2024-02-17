package com.paranid5.data.user.user_state_patch

import com.paranid5.core.entities.user.User

interface UserStatePatch {
    suspend fun patchUserState(user: User)
}