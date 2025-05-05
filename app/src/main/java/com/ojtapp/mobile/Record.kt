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

enum class SectorType(val value: String) {
    FURNITURE("FURNITURE"),
    FOOD_PROCESSING("FOOD PROCESSING"),
    HANDICRAFT("HANDICRAFT"),
    AGRI_HORTI("AGRI_HORTI"),
    MTL_ENGRING("MTL_ENGRING"),
    MARINE_AQUATIC("MARINE_AQUATIC"),
    ICT("ICT"),
    HEALTH("HEALTH"),
    ENERGY_ENV("ENERGY_ENV");

    companion object {
        fun from(value: String): SectorType? {
            return entries.firstOrNull { it.value.equals(value.trim(), ignoreCase = true) }
        }
    }
}

enum class GiaClass(val value: String) {
    CEST("CEST"),
    ELCAC("ELCAC"),
    RND("R&D"),
    CEST_ELCAC("CEST / ELCAC"),
    GIA("GIA");

    companion object {
        fun from(value: String): GiaClass? {
            return entries.firstOrNull { it.value.equals(value.trim(), ignoreCase = true) }
        }
    }
}

enum class Status(val value: String){
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    TERMINATED("Terminated")
}

enum class Type{
    GIA,
    SETUP
}