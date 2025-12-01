package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity

class ITagsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_income)

        val button: Button = findViewById(R.id.expenseButton)
        button.setOnClickListener {
            val intent = Intent(this, ETagsActivity::class.java)
            startActivity(intent)
        }

        //bottom ribbon functionality
        val bottomRibbon: bottomRibbon = findViewById(R.id.bottomRibbon)
        bottomRibbon.btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        bottomRibbon.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        bottomRibbon.btnTag.setOnClickListener {
            startActivity(Intent(this, ETagsActivity::class.java))
        }
        //end of bottom ribbon functionality
    }// end of onCreate
}

