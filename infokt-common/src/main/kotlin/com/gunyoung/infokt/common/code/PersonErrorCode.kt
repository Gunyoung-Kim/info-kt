package com.gunyoung.infokt.common.code;

enum class PersonErrorCode(
    val code: String,
    val description: String
) {
    PERSON_NOT_FOUNDED_ERROR("P001", "Person is not founded"),
    PERSON_DUPLICATION_FOUNDED_ERROR("P002", "Person duplication is founded"),
    RESOURCE_IS_NOT_MINE_ERROR("P003", "Access denied because resource is not yours")
}
