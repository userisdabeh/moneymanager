package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class CustomITagsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_itags)

        val bk2Itag: ImageButton = findViewById(R.id.bk2Itag)
        val expenseButton: Button = findViewById(R.id.expenseButton)

        bk2Itag.setOnClickListener {
            val intent = Intent(this, ITagsActivity::class.java)
            startActivity(intent)
        }

        expenseButton.setOnClickListener {
            val intent = Intent(this, CustomETagsActivity::class.java)
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
    }// end of onCreate
}