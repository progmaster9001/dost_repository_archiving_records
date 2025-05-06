package com.ojtapp.mobile.components.util

import com.ojtapp.mobile.model.GiaRecord
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.model.SetupRecord
import com.ojtapp.mobile.model.Type
import com.ojtapp.mobile.viewmodels.RecordState

class FilterBuilder<T : Record> {
    private val predicates = mutableListOf<(T) -> Boolean>()

    fun with(predicate: (T) -> Boolean): FilterBuilder<T> {
        predicates.add(predicate)
        return this
    }

    fun <R : Comparable<R>> withRange(selector: (T) -> R, range: ClosedRange<R>): FilterBuilder<T> {
        return with { item -> selector(item) in range }
    }

    fun <R : Comparable<R>> withMin(selector: (T) -> R, min: R): FilterBuilder<T> {
        return with { item -> selector(item) >= min }
    }

    fun <R : Comparable<R>> withMax(selector: (T) -> R, max: R): FilterBuilder<T> {
        return with { item -> selector(item) <= max }
    }

    fun build(): (T) -> Boolean = { item -> predicates.all { it(item) } }
}

interface FilterCriteria

data class SetupRecordFilterCriteria(
    val firmNameContains: String? = null,
    val proponentContains: String? = null,
    val minAmountApproved: Double? = null,
    val maxAmountApproved: Double? = null,
    val minYearApproved: Int? = null,
    val maxYearApproved: Int? = null,
    val locationContains: String? = null,
    val districtContains: String? = null,
    val statusContains: String? = null,
    val statusIn: List<String>? = null,
    val sectorContains: String? = null,
    val sectorIn: List<String>? = null
) : FilterCriteria

data class GiaRecordFilterCriteria(
    val projectTitleContains: String? = null,
    val locationContains: String? = null,
    val beneficiaryContains: String? = null,
    val remarksContains: String? = null,
    val minProjectCost: Double? = null,
    val maxProjectCost: Double? = null,
    val classContains: String? = null,
    val classesIn: List<String>? = null
) : FilterCriteria

fun buildSetupRecordFilter(criteria: SetupRecordFilterCriteria): (SetupRecord) -> Boolean {
    val builder = FilterBuilder<SetupRecord>()

    criteria.firmNameContains?.let { text ->
        builder.with { it.firmName.contains(text, ignoreCase = true) }
    }

    criteria.proponentContains?.let { text ->
        builder.with { it.proponent?.contains(text, ignoreCase = true) ?: false }
    }

    criteria.locationContains?.let { text ->
        builder.with { it.location?.contains(text, ignoreCase = true) ?: false }
    }

    criteria.districtContains?.let { text ->
        builder.with { it.district?.contains(text, ignoreCase = true) ?: false }
    }

    criteria.statusContains?.let { text ->
        builder.with { it.status.contains(text, ignoreCase = true) }
    }

    criteria.sectorContains?.let { text ->
        builder.with { it.sector.contains(text, ignoreCase = true) }
    }

    criteria.statusIn?.let { values ->
        builder.with { it.status in values }
    }

    criteria.sectorIn?.let { values ->
        builder.with { it.sector in values }
    }

    criteria.minAmountApproved?.let { min ->
        builder.withMin({ it.amountApproved ?: 0.0 }, min)
    }

    criteria.maxAmountApproved?.let { max ->
        builder.withMax({ it.amountApproved ?: 0.0 }, max)
    }

    criteria.minYearApproved?.let { min ->
        builder.withMin({ it.yearApproved ?: Int.MIN_VALUE }, min)
    }

    criteria.maxYearApproved?.let { max ->
        builder.withMax({ it.yearApproved ?: Int.MAX_VALUE }, max)
    }

    return builder.build()
}

fun buildGiaRecordFilter(criteria: GiaRecordFilterCriteria): (GiaRecord) -> Boolean {
    val builder = FilterBuilder<GiaRecord>()

    criteria.projectTitleContains?.let { text ->
        builder.with { it.projectTitle.contains(text, ignoreCase = true) }
    }

    criteria.beneficiaryContains?.let { text ->
        builder.with { it.beneficiary.contains(text, ignoreCase = true) }
    }

    criteria.locationContains?.let { text ->
        builder.with { it.location.contains(text, ignoreCase = true) }
    }

    criteria.remarksContains?.let { text ->
        builder.with { it.remarks?.contains(text, ignoreCase = true) ?: false }
    }

    criteria.classContains?.let { text ->
        builder.with { it.className.contains(text, ignoreCase = true) }
    }

    criteria.classesIn?.let { values ->
        builder.with { it.className in values }
    }

    criteria.minProjectCost?.let { min ->
        builder.withMin({ it.projectCost }, min)
    }

    criteria.maxProjectCost?.let { max ->
        builder.withMax({ it.projectCost }, max)
    }

    return builder.build()
}

fun filterRecords(
    state: RecordState,
    gia: GiaRecordFilterCriteria,
    setup: SetupRecordFilterCriteria
): RecordState {
    return when (state) {
        is RecordState.Error -> state
        RecordState.Loading -> state
        is RecordState.Success -> {
            val filtered = when (state.records.firstOrNull()) {
                is GiaRecord -> state.records
                    .filterIsInstance<GiaRecord>()
                    .filter(buildGiaRecordFilter(gia))

                is SetupRecord -> state.records
                    .filterIsInstance<SetupRecord>()
                    .filter(buildSetupRecordFilter(setup))

                else -> emptyList()
            }

            RecordState.Success(filtered)
        }
    }
}

fun countFilters(
    type: Type,
    giaCriteria: GiaRecordFilterCriteria,
    setupCriteria: SetupRecordFilterCriteria
): Int {
    return when (type) {
        Type.GIA -> listOfNotNull(
            giaCriteria.projectTitleContains,
            giaCriteria.locationContains,
            giaCriteria.beneficiaryContains,
            giaCriteria.remarksContains,
            giaCriteria.minProjectCost,
            giaCriteria.maxProjectCost,
            giaCriteria.classContains,
            giaCriteria.classesIn
        ).size

        Type.SETUP -> listOfNotNull(
            setupCriteria.firmNameContains,
            setupCriteria.proponentContains,
            setupCriteria.minAmountApproved,
            setupCriteria.maxAmountApproved,
            setupCriteria.minYearApproved,
            setupCriteria.maxYearApproved,
            setupCriteria.locationContains,
            setupCriteria.districtContains,
            setupCriteria.statusContains,
            setupCriteria.sectorContains,
            setupCriteria.sectorIn,
            setupCriteria.statusIn
        ).size
    }
}

fun GiaRecordFilterCriteria.isSame(
    projectTitle: String,
    location: String,
    classNameContains: String,
    beneficiaryContains: String,
    remarksContains: String,
    minProjectCost: String,
    maxProjectCost: String,
    selectedClasses: List<String>
): Boolean {
    val minCostDouble = minProjectCost.toDoubleOrNull()
    val maxCostDouble = maxProjectCost.toDoubleOrNull()

    return (this.projectTitleContains ?: "").trim() == projectTitle.trim() &&
            (this.locationContains ?: "").trim() == location.trim() &&
            (this.classContains ?: "").trim() == classNameContains.trim() &&
            (this.beneficiaryContains ?: "").trim() == beneficiaryContains.trim() &&
            (this.remarksContains ?: "").trim() == remarksContains.trim() &&
            (this.minProjectCost ?: 0.0) == (minCostDouble ?: 0.0) &&
            (this.maxProjectCost ?: 0.0) == (maxCostDouble ?: 0.0) &&
            this.classesIn.orEmpty() == selectedClasses
}

fun SetupRecordFilterCriteria.isSame(
    selectedSectors: List<String>,
    selectedStatuses: List<String>,
    proponentContains: String,
    firmNameContains: String,
    minYearApprovedStr: String,
    maxYearApprovedStr: String,
    minAmountApprovedStr: String,
    maxAmountApprovedStr: String
): Boolean {
    return (this.sectorIn.orEmpty() == selectedSectors) &&
            (this.statusIn.orEmpty() == selectedStatuses) &&
            this.proponentContains.orEmpty() == proponentContains &&
            this.firmNameContains.orEmpty() == firmNameContains &&
            (this.minYearApproved?.toString().orEmpty()) == minYearApprovedStr &&
            (this.maxYearApproved?.toString().orEmpty()) == maxYearApprovedStr &&
            (this.minAmountApproved?.toString().orEmpty()) == minAmountApprovedStr &&
            (this.maxAmountApproved?.toString().orEmpty()) == maxAmountApprovedStr
}

fun GiaRecordFilterCriteria.isEmpty(): Boolean {
    return locationContains == null &&
            projectTitleContains == null &&
            beneficiaryContains == null &&
            remarksContains == null &&
            minProjectCost == null &&
            maxProjectCost == null &&
            classContains == null &&
            classesIn.isNullOrEmpty()
}

fun SetupRecordFilterCriteria.isEmpty(): Boolean {
    return firmNameContains == null &&
            proponentContains == null &&
            minAmountApproved == null &&
            maxAmountApproved == null &&
            minYearApproved == null &&
            maxYearApproved == null &&
            locationContains == null &&
            districtContains == null &&
            statusContains == null &&
            sectorContains == null &&
            sectorIn.isNullOrEmpty() &&
            statusIn.isNullOrEmpty()
}