package com.reactive.demo

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.lang.management.ManagementFactory


@SpringBootApplication
class DemoApplication {

	private var logger = LoggerFactory.getLogger(DemoApplication::class.java)

	@PostConstruct
	fun logCoreCount() {
		val cores = Runtime.getRuntime().availableProcessors()
		logger.info("Number of CPU cores available to JVM: {}", cores)

		// Log JVM arguments to check if -XX:ActiveProcessorCount is applied
		val jvmArguments = ManagementFactory.getRuntimeMXBean().inputArguments
		logger.info("JVM arguments: {}", jvmArguments)
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
