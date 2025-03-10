package com.ojtapp.mobile

import kotlin.reflect.full.memberProperties

interface Record

fun getFieldValue(record: Record, fieldName: String): String {
    return try {
        val property = record::class.memberProperties.find { it.name == fieldName }
        property?.getter?.call(record)?.toString() ?: ""
    } catch (e: Exception) {
        ""
    }
}

enum class Type{
    GIA,
    SETUP
}