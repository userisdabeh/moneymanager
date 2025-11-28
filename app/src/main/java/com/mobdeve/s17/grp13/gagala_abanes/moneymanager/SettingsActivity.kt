package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val spinner: Spinner = findViewById(R.id.theme_dropdwn)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.theme_options, // defined in res/values/strings.xml
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val deleteButton: Button = findViewById(R.id.deldata)
        deleteButton.setOnClickListener {
            // Build confirmation dialog
            AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all data?")
                .setPositiveButton("Yes") { dialog, _ ->
                    // TODO: Add code to delete your data here
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}

