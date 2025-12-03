package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import android.view.View
import android.widget.AdapterView

class SettingsActivity : ComponentActivity() {

    //theme stuff
    private fun applyTheme(layout: View, mode: String) {
        when (mode) {
            "Default" -> layout.setBackgroundColor(getColor(R.color.app_default_bg))
            "Light" -> layout.setBackgroundColor(getColor(R.color.app_light_bg))
            "Dark" -> layout.setBackgroundColor(getColor(R.color.app_dark_bg))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val rootLayout = findViewById<View>(R.id.settingsRoot)
        val spinner: Spinner = findViewById(R.id.theme_dropdwn)
        val deleteButton: Button = findViewById(R.id.deldata)
        val bottomRibbon: BottomRibbon = findViewById(R.id.bottomRibbon)
        val prefs = getSharedPreferences("app_theme", MODE_PRIVATE)

        // --- Adapter ---
        val themes = resources.getStringArray(R.array.theme_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // --- Load saved theme ---
        val savedTheme = prefs.getString("theme", "Default")!!
        applyTheme(rootLayout, savedTheme)

        // --- Set spinner to saved theme ---
        val spinnerPosition = themes.indexOf(savedTheme).takeIf { it >= 0 } ?: 0
        spinner.setSelection(spinnerPosition, false)

        // --- Listener ---
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTheme = themes[position]
                prefs.edit().putString("theme", selectedTheme).apply()
                applyTheme(rootLayout, selectedTheme)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // --- Delete button ---
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all data?")
                .setPositiveButton("Yes") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        // --- Bottom ribbon ---
        bottomRibbon.btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        bottomRibbon.btnTag.setOnClickListener {
            startActivity(Intent(this, ETagsActivity::class.java))
        }
    }
}
