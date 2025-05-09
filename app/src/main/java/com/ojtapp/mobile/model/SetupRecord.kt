package com.ojtapp.mobile.model

import java.util.UUID
import kotlin.reflect.full.primaryConstructor

data class SetupRecord(
    val id: Int,
    val firmName: String,
    val proponent: String?,
    val amountApproved: Double?,
    val yearApproved: Int?,
    val fileLocation: String?,
    val location: String?,
    val district: String?,
    val sector: String,
    val status: String,
    val listOfEquipment: String?,
) : Record

val setupFieldNames = SetupRecord::class.primaryConstructor
    ?.parameters
    ?.mapNotNull { it.name }
    ?.filterNot { it.contains("fileLocation") }