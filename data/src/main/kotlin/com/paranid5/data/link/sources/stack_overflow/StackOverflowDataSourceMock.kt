package com.paranid5.data.link.sources.stack_overflow

import org.springframework.stereotype.Component

@Component
class StackOverflowDataSourceMock : StackOverflowDataSource by StackOverflowDataSourceInMemory()