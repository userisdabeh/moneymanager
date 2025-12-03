package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts

class ETagsActivity : ComponentActivity() {

    //theme stuff
    private fun applyTheme(layout: View, mode: String) {
        when (mode) {
            "Default" -> layout.setBackgroundColor(getColor(R.color.app_default_bg))
            "Light" -> layout.setBackgroundColor(getColor(R.color.app_light_bg))
            "Dark" -> layout.setBackgroundColor(getColor(R.color.app_dark_bg))
        }
    }

    private val customTagLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val iconName = result.data?.getStringExtra("selectedIcon")
            iconName?.let { addIconToGrid(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_expense)

        val toIncBtn: Button = findViewById(R.id.incomeButton)
        val toCtEBtn: Button = findViewById(R.id.moretags)

        //light mode stuff
        val prefs = getSharedPreferences("app_theme", MODE_PRIVATE)
        val savedTheme = prefs.getString("theme", "Default")!!

        val rootLayout = findViewById<View>(R.id.rootLayout)
        applyTheme(rootLayout, savedTheme)

        toIncBtn.setOnClickListener {
            startActivity(Intent(this, ITagsActivity::class.java))
        }

        // Launch CustomETagsActivity for result
        toCtEBtn.setOnClickListener {
            val intent = Intent(this, CustomETagsActivity::class.java)
            customTagLauncher.launch(intent)
        }

        // Bottom ribbon
        val bottomRibbon: BottomRibbon = findViewById(R.id.bottomRibbon)
        bottomRibbon.btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        bottomRibbon.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun addIconToGrid(iconName: String) {
        val gridLayout = findViewById<GridLayout>(R.id.expTagGrid)
        val resId = resources.getIdentifier(iconName, "drawable", packageName)
        if (resId == 0) return // skip if drawable not found

        val newButton = Button(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 80.dpToPx()
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(4.dpToPx(), 4.dpToPx(), 4.dpToPx(), 4.dpToPx())
            }
            background = ContextCompat.getDrawable(this@ETagsActivity, resId)
            text = ""
        }

        // Insert before "moretags" button
        val moreTagsIndex = (0 until gridLayout.childCount).firstOrNull {
            gridLayout.getChildAt(it).id == R.id.moretags
        } ?: gridLayout.childCount

        gridLayout.addView(newButton, moreTagsIndex)
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
