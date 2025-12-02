package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.content.Intent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.currSelector)
        button.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java) //CHANGE TO SWAP CURRENCY LATER
            startActivity(intent)
        }

        //bottom ribbon functionality
        val bottomRibbon: BottomRibbon = findViewById(R.id.bottomRibbon)
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
    }
}

