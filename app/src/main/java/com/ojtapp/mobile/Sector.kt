package com.ojtapp.mobile

data class Sector(
    val id: Int,
    val firmName: String,
    val components: String,
    val amountApproved: Double,
    val yearApproved: Int,
    val fileLocation: String?,
    val location: String?,
    val district: String?,
    val listOfEquipment: String?,
    val sectorId: Int?,
    val createdAt: String,
    val updatedAt: String,
    val status: String
) : Record