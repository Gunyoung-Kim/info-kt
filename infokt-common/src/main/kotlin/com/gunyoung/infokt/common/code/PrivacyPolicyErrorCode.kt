package com.gunyoung.infokt.common.code

enum class PrivacyPolicyErrorCode(
    private val code: String, private val description: String
) {
    PRIVACY_POLICY_VERSION_IS_NOT_VALID_ERROR("PP001", "Such version of privacy policy is not founded")
}