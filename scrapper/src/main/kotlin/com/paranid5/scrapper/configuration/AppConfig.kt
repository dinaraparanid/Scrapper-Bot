package com.paranid5.scrapper.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class AppConfig(val scheduler: Scheduler)

data class Scheduler(
    val isEnabled: Boolean,
    val interval: Duration,
    val forceCheckDelay: Duration
)