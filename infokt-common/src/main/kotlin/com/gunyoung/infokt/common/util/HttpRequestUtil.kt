package com.gunyoung.infokt.common.util

import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.getRequestIp() : String = getHeader("X-Real-IP") ?: remoteHost

fun HttpServletRequest.getStringOfParameters() : String =
    if (parameterMap.isEmpty()) {
        ""
    } else {
        "[ ${paramMapToString(parameterMap)}]"
    }

private fun paramMapToString(parameterMap: Map<String, Array<String>>) : String {
    val sb = StringBuilder()
    parameterMap.entries.forEach {
        writeKeyAndValueOfEntryTo(sb, it)
    }
    val stringOfParameter = sb.toString()
    return stringOfParameter.substring(0, stringOfParameter.length - 2)
}

fun writeKeyAndValueOfEntryTo(sb: StringBuilder, entry: Map.Entry<String, Array<String>>) {
    sb.append("${entry.key} : ")
    entry.value.forEach {
        sb.append("$it ")
    }
    sb.append(", ")
}
