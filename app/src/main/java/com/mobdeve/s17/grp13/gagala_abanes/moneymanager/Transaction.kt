package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

data class Transaction(
    val id: Int,
    val type: String,
    val amount: Double,
    val currency: String,
    val tag: String,
    val date: String,
    val comments: String?,
    val photos: String
)
