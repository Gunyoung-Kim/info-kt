package com.gunyoung.infokt.common

import com.gunyoung.infokt.common.config.CommonConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CommonConfig::class)
class TestConfig {
}