package com.paranid5.bot

import com.paranid5.bot.configuration.AppConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(AppConfig::class)
@ComponentScan(basePackages = ["com.paranid5"])
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}
