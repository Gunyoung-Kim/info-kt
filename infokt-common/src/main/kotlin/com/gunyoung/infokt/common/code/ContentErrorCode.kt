package com.gunyoung.infokt.common.code

enum class ContentErrorCode(
    val code: String,
    val description: String
) {
    CONTENT_NOT_FOUNDED_ERROR("C001", "Content not founded"),
    CONTENT_NUM_LIMIT_EXCEEDED_ERROR("C002", "Over the allocation count of content")
}