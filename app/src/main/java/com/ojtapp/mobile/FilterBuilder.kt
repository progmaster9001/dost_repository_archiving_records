package com.ojtapp.mobile

class FilterBuilder<T : Record> {
    private val predicates = mutableListOf<(T) -> Boolean>()

    fun with(predicate: (T) -> Boolean): FilterBuilder<T> {
        predicates.add(predicate)
        return this
    }

    fun <R : Comparable<R>> withRange(
        selector: (T) -> R,
        range: ClosedRange<R>
    ): FilterBuilder<T> {
        return with { item -> selector(item) in range }
    }

    fun <R : Comparable<R>> withMin(
        selector: (T) -> R,
        min: R
    ): FilterBuilder<T> {
        return with { item -> selector(item) >= min }
    }

    fun <R : Comparable<R>> withMax(
        selector: (T) -> R,
        max: R
    ): FilterBuilder<T> {
        return with { item -> selector(item) <= max }
    }

    fun build(): (T) -> Boolean = { item ->
        predicates.all { it(item) }
    }
}

interface FilterCriteria

data class SetupRecordFilterCriteria(
    val firmNameContains: String? = null,
    val componentsContains: String? = null,
    val minAmountApproved: Double? = null,
    val maxAmountApproved: Double? = null,
    val minYearApproved: Int? = null,
    val maxYearApproved: Int? = null,
    val locationContains: String? = null,
    val districtContains: String? = null,
    val listOfEquipmentContains: String? = null
) : FilterCriteria

data class GiaRecordFilterCriteria(
    val locationContains: String? = null,
    val beneficiaryContains: String? = null,
    val remarksContains: String? = null,
    val projectDurationContains: String? = null,
    val minProjectCost: Double? = null,
    val maxProjectCost: Double? = null,
    val classId: Int? = null,
    val qrcContains: String? = null
) : FilterCriteria

fun buildSetupRecordFilter(criteria: SetupRecordFilterCriteria): (SetupRecord) -> Boolean {
    val builder = FilterBuilder<SetupRecord>()

    criteria.firmNameContains?.let {
        builder.with {
            it.firmName.contains(it.firmName, ignoreCase = true)
        }
    }

    criteria.componentsContains?.let {
        builder.with { it.components?.contains(it.components, ignoreCase = true) ?: false }
    }

    criteria.locationContains?.let {
        builder.with { it.location?.contains(it.location, ignoreCase = true) ?: false }
    }

    criteria.districtContains?.let {
        builder.with { it.district?.contains(it.district, ignoreCase = true) ?: false }
    }

    criteria.listOfEquipmentContains?.let {
        builder.with { it.listOfEquipment?.contains(it.listOfEquipment, ignoreCase = true) ?: false }
    }

    criteria.minAmountApproved?.let {
        builder.withMin({ it.amountApproved ?: 0.0 }, it)
    }

    criteria.maxAmountApproved?.let {
        builder.withMax({ it.amountApproved ?: 0.0 }, it)
    }

    criteria.minYearApproved?.let { minYear ->
        builder.withMin({ it.yearApproved ?: Int.MIN_VALUE }, minYear)
    }

    criteria.maxYearApproved?.let { maxYear ->
        builder.withMax({ it.yearApproved ?: Int.MAX_VALUE }, maxYear)
    }

    return builder.build()
}

fun buildGiaRecordFilter(criteria: GiaRecordFilterCriteria): (GiaRecord) -> Boolean {
    val builder = FilterBuilder<GiaRecord>()

    criteria.locationContains?.let {
        builder.with { it.location.contains(it.location, ignoreCase = true) }
    }

    criteria.beneficiaryContains?.let {
        builder.with { it.beneficiary.contains(it.beneficiary, ignoreCase = true) }
    }

    criteria.remarksContains?.let {
        builder.with { it.remarks?.contains(it.remarks, ignoreCase = true) ?: false }
    }

    criteria.projectDurationContains?.let {
        builder.with { it.projectDuration.contains(it.projectDuration, ignoreCase = true) }
    }

    criteria.minProjectCost?.let {
        builder.withMin({ it.projectCost }, it)
    }

    criteria.maxProjectCost?.let {
        builder.withMax({ it.projectCost }, it)
    }

    criteria.classId?.let { id ->
        builder.with { it.classId == id }
    }

    criteria.qrcContains?.let {
        builder.with { it.qrc?.contains(it.qrc, ignoreCase = true) ?: false }
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

fun countFilters(type: Type, giaCriteria: GiaRecordFilterCriteria, setupCriteria: SetupRecordFilterCriteria): Int {
    return when (type) {
        Type.GIA -> listOfNotNull(
            giaCriteria.locationContains,
            giaCriteria.beneficiaryContains,
            giaCriteria.remarksContains,
            giaCriteria.projectDurationContains,
            giaCriteria.minProjectCost,
            giaCriteria.maxProjectCost,
            giaCriteria.classId,
            giaCriteria.qrcContains
        ).size

        Type.SETUP -> listOfNotNull(
            setupCriteria.firmNameContains,
            setupCriteria.componentsContains,
            setupCriteria.minAmountApproved,
            setupCriteria.maxAmountApproved,
            setupCriteria.minYearApproved,
            setupCriteria.maxYearApproved,
            setupCriteria.locationContains,
            setupCriteria.districtContains,
            setupCriteria.listOfEquipmentContains
        ).size
    }
}

fun GiaRecordFilterCriteria.isEmpty(): Boolean {
    return locationContains == null &&
            beneficiaryContains == null &&
            remarksContains == null &&
            projectDurationContains == null &&
            minProjectCost == null &&
            maxProjectCost == null &&
            classId == null &&
            qrcContains == null
}

fun SetupRecordFilterCriteria.isEmpty(): Boolean {
    return firmNameContains == null &&
            componentsContains == null &&
            minAmountApproved == null &&
            maxAmountApproved == null &&
            minYearApproved == null &&
            maxYearApproved == null &&
            locationContains == null &&
            districtContains == null &&
            listOfEquipmentContains == null
}