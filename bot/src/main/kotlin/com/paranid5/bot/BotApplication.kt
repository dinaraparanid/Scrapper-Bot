package com.paranid5.bot

import com.paranid5.bot.configuration.AppConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppConfig::class)
class BotApplication

fun main(args: Array<String>) {
	runApplication<BotApplication>(*args)
}
