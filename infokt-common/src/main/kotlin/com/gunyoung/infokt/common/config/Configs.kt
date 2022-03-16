package com.gunyoung.infokt.common.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@Import(
    ScanConfigs::class,
    SecurityConfig::class
)
@EnableJpaAuditing
class CommonConfig {
}

@Configuration
@EntityScan("com.gunyoung.infokt.common")
@ComponentScan("com.gunyoung.infokt.common")
class ScanConfigs {
}