package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout

class BottomRibbon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    val btnTag: LinearLayout
    val btnHome: LinearLayout
    val btnSettings: LinearLayout

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.bottomribbon, this, true)
        btnTag = findViewById(R.id.btnTag)
        btnHome = findViewById(R.id.btnHome)
        btnSettings = findViewById(R.id.btnSettings)
    }

    fun highlightButton(activeButtonId: Int) {
        val buttons = listOf(btnTag, btnHome, btnSettings)
        buttons.forEach { it.setBackgroundColor(resources.getColor(android.R.color.black)) }
        findViewById<Button>(activeButtonId)?.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
    }
}
