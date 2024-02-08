package com.paranid5.scrapper

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestScrapperApplication {

	@Bean
	@ServiceConnection
	fun kafkaContainer(): KafkaContainer {
		return KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
	}

	@Bean
	@ServiceConnection
	fun postgresContainer(): PostgreSQLContainer<*> {
		return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
	}

}

fun main(args: Array<String>) {
	fromApplication<ScrapperApplication>().with(TestScrapperApplication::class).run(*args)
}
