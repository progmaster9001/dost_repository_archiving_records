package com.ojtapp.mobile

import kotlin.reflect.full.primaryConstructor

data class SetupRecord(
    val id: Int,
    val firmName: String,
    val components: String,
    val amountApproved: String,
    val yearApproved: Int,
    val status: String,
    val sectorName: String
): Record

val setupFieldNames = SetupRecord::class.primaryConstructor?.parameters?.map { it.name }