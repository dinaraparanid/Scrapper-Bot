package com.paranid5.scrapper

import com.paranid5.scrapper.configuration.AppConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppConfig::class)
class ScrapperApplication

fun main(args: Array<String>) {
	runApplication<ScrapperApplication>(*args)
}
