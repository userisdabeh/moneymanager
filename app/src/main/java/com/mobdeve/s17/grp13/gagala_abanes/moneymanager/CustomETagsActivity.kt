package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity

class CustomETagsActivity : ComponentActivity() {
    private var selectedIcon: String = "edu"
    private var selectedColor: String = "grey"
    private lateinit var eTagIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_etags)

        eTagIcon = findViewById(R.id.eTag_icon)

        val bk2Etag: ImageButton = findViewById(R.id.bk2Etag)
        val incomeButton: Button = findViewById(R.id.incomeButton)
        val expAddButton: Button = findViewById(R.id.expAddButton)

        // Navigation buttons
        bk2Etag.setOnClickListener { finish() }
        incomeButton.setOnClickListener {
            startActivity(Intent(this, CustomITagsActivity::class.java))
        }

        // Color buttons
        findViewById<Button>(R.id.ech_blue).setOnClickListener { selectedColor = "blu"; updateIcon() }
        findViewById<Button>(R.id.ech_red).setOnClickListener { selectedColor = "red"; updateIcon() }
        findViewById<Button>(R.id.ech_green).setOnClickListener { selectedColor = "grn"; updateIcon() }
        findViewById<Button>(R.id.ech_yellow).setOnClickListener { selectedColor = "ylw"; updateIcon() }
        findViewById<Button>(R.id.ech_orange).setOnClickListener { selectedColor = "org"; updateIcon() }
        findViewById<Button>(R.id.ech_grey).setOnClickListener { selectedColor = "grey"; updateIcon() }

        // Icon buttons
        findViewById<Button>(R.id.education).setOnClickListener { selectedIcon = "edu"; updateIcon() }
        findViewById<Button>(R.id.food).setOnClickListener { selectedIcon = "food"; updateIcon() }
        findViewById<Button>(R.id.leisure).setOnClickListener { selectedIcon = "les"; updateIcon() }
        findViewById<Button>(R.id.home).setOnClickListener { selectedIcon = "home"; updateIcon() }
        findViewById<Button>(R.id.transpo).setOnClickListener { selectedIcon = "transpo"; updateIcon() }

        // **Send selected icon back to ETagsActivity only on Add button**
        expAddButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("selectedIcon", "${selectedColor}${selectedIcon}")
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Bottom ribbon
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
    }

    private fun updateIcon() {
        val name = "${selectedColor}${selectedIcon}"
        val resId = resources.getIdentifier(name, "drawable", packageName)
        eTagIcon.setImageResource(if (resId != 0) resId else R.drawable.clrgry)
    }
}
