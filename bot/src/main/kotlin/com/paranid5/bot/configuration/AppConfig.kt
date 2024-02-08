package com.paranid5.bot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
internal data class AppConfig(val telegramToken: String)