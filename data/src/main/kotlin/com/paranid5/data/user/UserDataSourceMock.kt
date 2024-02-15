package com.paranid5.data.user

import org.springframework.stereotype.Component

@Component
class UserDataSourceMock : UserDataSource by UserDataSourceInMemory()