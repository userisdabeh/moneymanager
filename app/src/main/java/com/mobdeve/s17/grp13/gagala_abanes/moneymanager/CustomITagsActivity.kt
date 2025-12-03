package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import android.view.View

class CustomITagsActivity : ComponentActivity() {

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

        //light mode stuff
        val prefs = getSharedPreferences("app_theme", MODE_PRIVATE)
        val savedTheme = prefs.getString("theme", "Default")!!

        val rootLayout = findViewById<View>(R.id.rootLayout)
        applyTheme(rootLayout, savedTheme)

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