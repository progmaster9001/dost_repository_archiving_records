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

data class GiaRecordFilterCriteria(
    val location: String? = null,
    val minProjectCost: Int? = null,
    val maxProjectCost: Int? = null,
    val classNameContains: String? = null,
    val beneficiaryContains: String? = null,
    val projectDurationRange: IntRange? = null, // assuming duration in months or days
    val remarksContains: String? = null
) : FilterCriteria

data class SetupRecordFilterCriteria(
    val sectorName: String? = null,
    val sectorNameIn: List<String>? = null,
    val minYearApproved: Int? = null,
    val maxYearApproved: Int? = null,
    val minAmountApproved: Int? = null,
    val maxAmountApproved: Int? = null,
    val status: String? = null,
    val statusIn: List<String>? = null,
    val proponentContains: String? = null,
    val firmNameContains: String? = null
) : FilterCriteria

fun buildGiaRecordFilter(criteria: GiaRecordFilterCriteria): (GiaRecord) -> Boolean {
    val builder = FilterBuilder<GiaRecord>()

    criteria.location?.let {
        builder.with { record -> record.location.equals(it, ignoreCase = true) }
    }

    criteria.classNameContains?.let {
        builder.with { record -> record.className.contains(it, ignoreCase = true) }
    }

    criteria.beneficiaryContains?.let {
        builder.with { record -> record.beneficiary.contains(it, ignoreCase = true) }
    }

    criteria.remarksContains?.let {
        builder.with { record -> record.remarks.contains(it, ignoreCase = true) }
    }

    criteria.projectDurationRange?.let { range ->
        builder.with { record ->
            val durationValue = record.projectDuration.filter { it.isDigit() }.toIntOrNull() ?: 0
            durationValue in range
        }
    }

    criteria.minProjectCost?.let { min ->
        builder.withMin({ it.projectCost.toIntOrNull() ?: 0 }, min)
    }

    criteria.maxProjectCost?.let { max ->
        builder.withMax({ it.projectCost.toIntOrNull() ?: 0 }, max)
    }

    return builder.build()
}

fun buildSetupRecordFilter(criteria: SetupRecordFilterCriteria): (SetupRecord) -> Boolean {
    val builder = FilterBuilder<SetupRecord>()

    criteria.sectorName?.let {
        builder.with { record -> record.sectorName.equals(it, ignoreCase = true) }
    }

    criteria.sectorNameIn?.takeIf { it.isNotEmpty() }?.let { names ->
        builder.with { record -> names.any { name -> record.sectorName.equals(name, ignoreCase = true) } }
    }

    criteria.status?.let {
        builder.with { record -> record.status.equals(it, ignoreCase = true) }
    }

    criteria.statusIn?.takeIf { it.isNotEmpty() }?.let { statuses ->
        builder.with { record -> statuses.any { status -> record.status.equals(status, ignoreCase = true) } }
    }

    criteria.proponentContains?.let {
        builder.with { record -> record.proponent.contains(it, ignoreCase = true) }
    }

    criteria.firmNameContains?.let {
        builder.with { record -> record.firmName.contains(it, ignoreCase = true) }
    }

    criteria.minYearApproved?.let { min ->
        builder.withMin(SetupRecord::yearApproved, min)
    }

    criteria.maxYearApproved?.let { max ->
        builder.withMax(SetupRecord::yearApproved, max)
    }

    criteria.minAmountApproved?.let { min ->
        builder.withMin({ it.amountApproved.toIntOrNull() ?: 0 }, min)
    }

    criteria.maxAmountApproved?.let { max ->
        builder.withMax({ it.amountApproved.toIntOrNull() ?: 0 }, max)
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