package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class ITagsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_income)

        val toExpBtn: Button = findViewById(R.id.expenseButton)
        val toCtIBtn: Button = findViewById(R.id.moretags)

        toExpBtn.setOnClickListener {
            val intent = Intent(this, ETagsActivity::class.java)
            startActivity(intent)
        }

        toCtIBtn.setOnClickListener {
            val intent = Intent(this, CustomITagsActivity::class.java)
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

