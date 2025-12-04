package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import android.view.View
import android.widget.ImageView

class CustomITagsActivity : ComponentActivity() {

    private var selectedIcon: String = "edu"
    private var selectedColor: String = "grey"
    private lateinit var iTagIcon: ImageView
    private lateinit var db: TagDatabase

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

        iTagIcon = findViewById(R.id.iTag_icon)

        val bk2Itag: ImageButton = findViewById(R.id.bk2Itag)
        val expenseButton: Button = findViewById(R.id.expenseButton) //to expense side
        val incAddButton: Button = findViewById(R.id.incAddButton)
        db = TagDatabase(this)


        //back to main income tags
        bk2Itag.setOnClickListener { finish() }
        expenseButton.setOnClickListener {
            val intent = Intent(this, ITagsActivity::class.java)
            startActivity(intent)
        }

        //color choices
        findViewById<Button>(R.id.ich_blue).setOnClickListener { selectedColor = "blu"; updateIcon() }
        findViewById<Button>(R.id.ich_red).setOnClickListener { selectedColor = "red"; updateIcon() }
        findViewById<Button>(R.id.ich_green).setOnClickListener { selectedColor = "grn"; updateIcon() }
        findViewById<Button>(R.id.ich_yellow).setOnClickListener { selectedColor = "ylw"; updateIcon() }
        findViewById<Button>(R.id.ich_orange).setOnClickListener { selectedColor = "org"; updateIcon() }
        findViewById<Button>(R.id.ich_grey).setOnClickListener { selectedColor = "grey"; updateIcon() }

        //icon choices
        findViewById<Button>(R.id.education).setOnClickListener { selectedIcon = "edu"; updateIcon() }
        findViewById<Button>(R.id.food).setOnClickListener { selectedIcon = "food"; updateIcon() }
        findViewById<Button>(R.id.leisure).setOnClickListener { selectedIcon = "les"; updateIcon() }
        findViewById<Button>(R.id.home).setOnClickListener { selectedIcon = "home"; updateIcon() }
        findViewById<Button>(R.id.transpo).setOnClickListener { selectedIcon = "transpo"; updateIcon() }

        incAddButton.setOnClickListener {
            val finalName = "${selectedColor}${selectedIcon}"

            // insert into income tags table
            db.insertIncomeTag(finalName)

            val resultIntent = Intent().apply {
                putExtra("selectedIcon", finalName)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
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
        }//to home page
        bottomRibbon.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }//to settings page
        bottomRibbon.btnTag.setOnClickListener {
            startActivity(Intent(this, ETagsActivity::class.java))
        }//to expense tags page
    }// end of onCreate

    private fun updateIcon() {
        val name = "${selectedColor}${selectedIcon}"
        val resId = resources.getIdentifier(name, "drawable", packageName)
        iTagIcon.setImageResource(if (resId != 0) resId else R.drawable.clrgry)
    }
}//end of main class