package com.ojtapp.mobile.components.util

fun Double.formatAsPeso(): String {
    val formatter = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("en", "PH"))
    return formatter.format(this)
}