package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import android.view.View

class ITagsActivity : ComponentActivity() {

    private fun applyTheme(layout: View, mode: String) {
        when (mode) {
            "Default" -> layout.setBackgroundColor(getColor(R.color.app_default_bg))
            "Light" -> layout.setBackgroundColor(getColor(R.color.app_light_bg))
            "Dark" -> layout.setBackgroundColor(getColor(R.color.app_dark_bg))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_income)

        val toExpBtn: Button = findViewById(R.id.expenseButton)
        val toCtIBtn: Button = findViewById(R.id.moretags)

        //light mode stuff
        val prefs = getSharedPreferences("app_theme", MODE_PRIVATE)
        val savedTheme = prefs.getString("theme", "Default")!!

        val rootLayout = findViewById<View>(R.id.rootLayout)
        applyTheme(rootLayout, savedTheme)

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

