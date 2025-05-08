package com.ojtapp.mobile.model

import com.ojtapp.mobile.components.util.formatAsPeso
import kotlin.reflect.full.memberProperties

interface Record

fun getFieldValue(record: Record, fieldName: String): String {
    return try {
        val property = record::class.memberProperties.find { it.name == fieldName }
        val value = property?.getter?.call(record)
        return if(value is Double){ value.formatAsPeso() } else{ value.toString() }
    } catch (e: Exception) {
        "Error Field"
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