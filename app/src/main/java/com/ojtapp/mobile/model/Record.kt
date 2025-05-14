package com.ojtapp.mobile.model

import androidx.compose.ui.graphics.Color
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

val giaClassColors = mapOf(
    GiaClass.CEST to Color(0xFF1E88E5),        // Blue
    GiaClass.ELCAC to Color(0xFFD32F2F),       // Red
    GiaClass.RND to Color(0xFF388E3C),         // Green
    GiaClass.CEST_ELCAC to Color(0xFFFBC02D),  // Yellow
    GiaClass.GIA to Color(0xFF8E24AA)          // Purple
)

val sectorTypeColors = mapOf(
    SectorType.FURNITURE to Color(0xFF6D4C41),       // Brown
    SectorType.FOOD_PROCESSING to Color(0xFFFF7043), // Orange
    SectorType.HANDICRAFT to Color(0xFFAB47BC),      // Lavender
    SectorType.AGRI_HORTI to Color(0xFF66BB6A),      // Light Green
    SectorType.MTL_ENGRING to Color(0xFF42A5F5),     // Light Blue
    SectorType.MARINE_AQUATIC to Color(0xFF26C6DA),  // Teal
    SectorType.ICT to Color(0xFFEC407A),             // Pink
    SectorType.HEALTH to Color(0xFF7E57C2),          // Deep Purple
    SectorType.ENERGY_ENV to Color(0xFFFFCA28)       // Yellow
)

enum class Status(val value: String){
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    TERMINATED("Terminated")
}

enum class Type{
    GIA,
    SETUP
}