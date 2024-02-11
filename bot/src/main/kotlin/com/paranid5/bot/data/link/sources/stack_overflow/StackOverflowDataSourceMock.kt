package com.paranid5.bot.data.link.sources.stack_overflow

import org.springframework.stereotype.Component

@Component("so_mock")
class StackOverflowDataSourceMock : StackOverflowDataSource by StackOverflowDataSourceInMemory()