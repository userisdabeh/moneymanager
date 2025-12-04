package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts

class ITagsActivity : ComponentActivity() {

    private lateinit var db: TagDatabase

    //theme stuff
    private fun applyTheme(layout: View, mode: String) {
        when (mode) {
            "Default" -> layout.setBackgroundColor(getColor(R.color.app_default_bg))
            "Light" -> layout.setBackgroundColor(getColor(R.color.app_light_bg))
            "Dark" -> layout.setBackgroundColor(getColor(R.color.app_dark_bg))
        }
    }

    //
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
        setContentView(R.layout.activity_tags_income)

        val toExpBtn: Button = findViewById(R.id.expenseButton) //to expense side
        val toCtIBtn: Button = findViewById(R.id.moretags) //to custom tags

        //light mode stuff
        val prefs = getSharedPreferences("app_theme", MODE_PRIVATE)
        val savedTheme = prefs.getString("theme", "Default")!!

        val rootLayout = findViewById<View>(R.id.rootLayout)
        applyTheme(rootLayout, savedTheme)

        db = TagDatabase(this)
        loadSavedIcons()

        toExpBtn.setOnClickListener {
            startActivity(Intent(this, ETagsActivity::class.java))
        }

        // Launch CustomETagsActivity for result
        toCtIBtn.setOnClickListener {
            val intent = Intent(this, CustomITagsActivity::class.java)
            customTagLauncher.launch(intent)
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

    private fun loadSavedIcons() {
        val saved = db.getAllIncomeTags()

        saved.forEach { iconName ->
            addIconToGrid(iconName)
        }
    }

    private fun addIconToGrid(iconName: String) {
        val gridLayout = findViewById<GridLayout>(R.id.incTagGrid)
        val resId = resources.getIdentifier(iconName, "drawable", packageName)
        if (resId == 0) return

        val newButton = Button(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 80.dpToPx()
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(4.dpToPx(), 4.dpToPx(), 4.dpToPx(), 4.dpToPx())
            }
            background = ContextCompat.getDrawable(this@ITagsActivity, resId)
            text = ""

            //hold to delete
            setOnLongClickListener {
                confirmDelete(this, iconName)
                true
            }
        }

        val moreTagsIndex = (0 until gridLayout.childCount).firstOrNull {
            gridLayout.getChildAt(it).id == R.id.moretags
        } ?: gridLayout.childCount

        gridLayout.addView(newButton, moreTagsIndex)
    }

    private fun confirmDelete(btn: Button, iconName: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Delete Tag?")
            .setMessage("Remove this tag permanently?")
            .setPositiveButton("Delete") { _, _ ->
                db.deleteIncomeTag(iconName)

                val grid = findViewById<GridLayout>(R.id.incTagGrid)
                grid.removeView(btn)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}