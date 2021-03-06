package com.gunyoung.infokt.common.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@Import(
    ScanConfigs::class,
    SecurityConfig::class
)
@EnableJpaAuditing
@EnableJpaRepositories("com.gunyoung.infokt.common")
class CommonConfig {
}

@Configuration
@EntityScan("com.gunyoung.infokt.common")
@ComponentScan(value = ["com.gunyoung.infokt.common"])
class ScanConfigs {
}