package com.paranid5.bot.data.user

import org.springframework.stereotype.Component

@Component("user_src_mock")
class UserDataSourceMock : UserDataSource by UserDataSourceInMemory()