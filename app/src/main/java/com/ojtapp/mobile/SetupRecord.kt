package com.ojtapp.mobile

data class SetupRecord(
    val id: Int,
    val firmName: String,
    val components: String,
    val amountApproved: String,
    val yearApproved: Int,
    val status: String,
    val sectorName: String
): Record