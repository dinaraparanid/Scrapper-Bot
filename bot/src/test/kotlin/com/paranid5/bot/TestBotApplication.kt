package com.paranid5.bot

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestBotApplication {
    @Bean
    @ServiceConnection
    fun kafkaContainer(): KafkaContainer =
        KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))

}

fun main(args: Array<String>) {
    fromApplication<BotApplication>()
        .with(TestBotApplication::class)
        .run(*args)
}
