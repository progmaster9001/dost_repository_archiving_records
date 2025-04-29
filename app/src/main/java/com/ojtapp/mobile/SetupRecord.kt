package com.ojtapp.mobile

import kotlin.reflect.full.primaryConstructor

data class SetupRecord(
    val id: Int,
    val firmName: String,
    val components: String?,
    val amountApproved: Double?,
    val yearApproved: Int?,
    val fileLocation: String,
    val location: String?,
    val district: String?,
    val listOfEquipment: String?,
    val dateAdded: String
) : Record

val setupFieldNames = SetupRecord::class.primaryConstructor?.parameters?.map { it.name }