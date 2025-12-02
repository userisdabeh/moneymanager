package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val rootLayout = findViewById<android.view.View>(R.id.settingsRoot)

        val spinner: Spinner = findViewById(R.id.theme_dropdwn)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.theme_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                when (spinner.selectedItem.toString()) {
                    "Default" -> rootLayout.setBackgroundColor(getColor(R.color.app_default_bg))
                    "Light"   -> rootLayout.setBackgroundColor(getColor(R.color.app_light_bg))   // white
                    "Dark"    -> rootLayout.setBackgroundColor(getColor(R.color.app_dark_bg))    // dark gray
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })

        val deleteButton: Button = findViewById(R.id.deldata)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all data?")
                .setPositiveButton("Yes") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        val bottomRibbon: BottomRibbon = findViewById(R.id.bottomRibbon)
        bottomRibbon.btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        bottomRibbon.btnTag.setOnClickListener {
            startActivity(Intent(this, ETagsActivity::class.java))
        }
    }
}


