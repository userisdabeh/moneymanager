package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s17.grp13.gagala_abanes.moneymanager.TagAdapter

class MainActivity : ComponentActivity() {

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionDb: TransactionDatabase

    private lateinit var recyclerView: RecyclerView

    //theme stuff
    private fun applyTheme(root: View, mode: String) {
        when (mode) {
            "Default" -> root.setBackgroundColor(getColor(R.color.app_default_bg))
            "Light" -> root.setBackgroundColor(getColor(R.color.app_light_bg))
            "Dark" -> root.setBackgroundColor(getColor(R.color.app_dark_bg))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootLayout = findViewById<View>(R.id.main_root) // Add this ID to root ConstraintLayout in XML

        // --- Load theme from SharedPreferences ---
        val prefs = getSharedPreferences("app_theme", MODE_PRIVATE)
        val savedTheme = prefs.getString("theme", "Default")!!
        applyTheme(rootLayout, savedTheme)

        val button: Button = findViewById(R.id.currSelector)
        button.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java) //CHANGE TO SWAP CURRENCY LATER
            startActivity(intent)
        }

        val donutCard: View = findViewById(R.id.donutCard)
        val btnAdd: ImageButton = donutCard.findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            addTransactionLauncher.launch(intent)
        }

        transactionDb = TransactionDatabase(this)
        transactionAdapter = TransactionAdapter(emptyList())

        recyclerView = findViewById(R.id.mainRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionAdapter

        loadTransactions()

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

    private val addTransactionLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
            if (result.resultCode == RESULT_OK) {
                loadTransactions()
            }
    }

    private fun loadTransactions() {
        val transactions = transactionDb.getAllTransactions()
        transactionAdapter.updateData(transactions)
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
    }
}

