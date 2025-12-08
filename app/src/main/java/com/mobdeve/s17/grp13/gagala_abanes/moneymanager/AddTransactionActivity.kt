package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException

class AddTransactionActivity: ComponentActivity() {

    private lateinit var gridLayout: GridLayout
    private var selectedTagButton: ImageButton? = null
    private var selectedTransactionType = "Expense"
    private var selectedTag: String? = null

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoUri: Uri

    private lateinit var transactionDb: TransactionDatabase
    private lateinit var tagDb: TagDatabase

    private lateinit var spinner: Spinner
    private lateinit var dateText: TextView
    private lateinit var addTransactionBtn: Button
    private lateinit var amountEditText: EditText
    private lateinit var commentsEditText: EditText


    fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    private val CAMERA_REQUEST_CODE = 999

    private val MAX_PHOTOS = 6

    private fun applyTheme(layout: View, mode: String) {
        when (mode) {
            "Default" -> layout.setBackgroundColor(getColor(R.color.app_default_bg))
            "Light" -> layout.setBackgroundColor(getColor(R.color.app_light_bg))
            "Dark" -> layout.setBackgroundColor(getColor(R.color.app_dark_bg))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        spinner = findViewById(R.id.currencySpinner)
        val categories = resources.getStringArray(R.array.currency_options)

        tagDb = TagDatabase(this)
        transactionDb = TransactionDatabase(this)

        val expenseTags = tagDb.getAllExpenseTags()
        val incomeTags = tagDb.getAllIncomeTags()

        var selectedButton: ImageButton? = null

        val dateInput: ImageButton = findViewById(R.id.dateSelector)
        dateText = findViewById(R.id.selectedDate)

        addTransactionBtn = findViewById(R.id.addTransactionBtn)
        amountEditText = findViewById(R.id.amountEditText)
        commentsEditText = findViewById(R.id.commentsEditText)

        val calendar = java.util.Calendar.getInstance()
        val todayString = "${calendar.get(java.util.Calendar.MONTH) + 1}/${calendar.get(java.util.Calendar.DAY_OF_MONTH)}/${calendar.get(java.util.Calendar.YEAR)}"

        gridLayout = findViewById(R.id.tagGrid)

        val expenseBtn: Button = findViewById(R.id.expenseBtn)
        val incomeBtn: Button = findViewById(R.id.incomeBtn)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCurrency = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing
            }
        }

        selectedTransactionType = "Expense"
        expenseBtn.setBackgroundColor(Color.LTGRAY)
        incomeBtn.setBackgroundColor(Color.BLACK)
        updateTagIcons(expenseTags)

        expenseBtn.setOnClickListener {
            selectedTransactionType = "Expense"
            expenseBtn.setBackgroundColor(Color.LTGRAY)
            incomeBtn.setBackgroundColor(Color.BLACK)
            updateTagIcons(expenseTags)
        }

        incomeBtn.setOnClickListener {
            selectedTransactionType = "Income"
            incomeBtn.setBackgroundColor(Color.LTGRAY)
            expenseBtn.setBackgroundColor(Color.BLACK)
            updateTagIcons(incomeTags)
        }

        updateTagIcons(expenseTags)

        dateText.text = todayString

        dateInput.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val year = calendar.get(java.util.Calendar.YEAR)
            val month = calendar.get(java.util.Calendar.MONTH)
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

            val datePickerDialog = android.app.DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val dateString = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                    dateText.text = dateString
                }, year, month, day
            )

            datePickerDialog.show()
        }

        val photoList = mutableListOf<Uri>()
        photoAdapter = PhotoAdapter(photoList) {
            showPhotoOptions()
        }

        val recyclerView: RecyclerView = findViewById(R.id.photoRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = photoAdapter

        saveTransaction()

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
    }

    private fun updateTagIcons(tags: List<String>) {
        gridLayout.removeAllViews()
        selectedTagButton = null

        for (iconName in tags) {
            val drawableId = resources.getIdentifier(iconName, "drawable", packageName)

            val button = ImageButton(this).apply {
                setImageResource(drawableId)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setPadding(16, 16, 16,16)
                setBackgroundColor(Color.TRANSPARENT)
            }

            val params = GridLayout.LayoutParams().apply{
                width = 80.dpToPx()
                height = 80.dpToPx()
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.LEFT)
                setMargins(8,8,8,8)
            }

            gridLayout.addView(button, params)

            button.setOnClickListener {
                selectedTagButton?.setBackgroundColor(Color.TRANSPARENT)
                button.setBackgroundColor(Color.LTGRAY)
                selectedTagButton = button
                selectedTag = iconName
            }
        }

    }

    private fun showPhotoOptions() {
        if (photoAdapter.photos.size >= MAX_PHOTOS){
            Toast.makeText(this, "You can only add $MAX_PHOTOS photos", Toast.LENGTH_SHORT).show()
            return
        }

        val options = arrayOf("Camera", "Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setItems(options) {
            dialog, which ->
                when (which) {
                    0 -> checkCameraPermission()
                    1 -> openGallery()
                }
        }

        builder.show()
    }

    private fun checkCameraPermission() {
        if(checkSelfPermission(CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(CAMERA_PERMISSION), CAMERA_REQUEST_CODE)
        } else {
            openCamera()
        }
    }

    private val REQUEST_CAMERA = 100
    private val REQUEST_GALLERY = 101

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }

        photoFile?.also {
            photoUri = FileProvider.getUriForFile(this, "${packageName}.provider", it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir: File? = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "photo_${System.currentTimeMillis()}",
            ".jpg",
            storageDir
        )
    }

    private fun openGallery() {
        val intent = android.content.Intent(android.content.Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                   addPhotoAdapter(photoUri)
                }
                REQUEST_GALLERY -> {
                    val selectedUri = data?.data
                    selectedUri?.let {
                        addPhotoAdapter(it)
                    }
                }
            }
        }
    }

    private fun addPhotoAdapter(uri: Uri) {
        photoAdapter.addPhoto(uri)
    }

    private fun saveBitmapToUri(bitmap: android.graphics.Bitmap) : Uri {
        val path = android.provider.MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    @Deprecated ("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::photoUri.isInitialized) {
            outState.putString("photoUri", photoUri.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.getString("photoUri")?.let {
            photoUri = Uri.parse(it)
        }
    }

    private fun saveTransaction() {
        addTransactionBtn.setOnClickListener {
            val enteredAmount = amountEditText.text.toString()
            if(enteredAmount.isBlank()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedTag == null) {
                Toast.makeText(this, "Please select a tag", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = enteredAmount.toDouble()

            val photoUriStrings: List<String> = photoAdapter.photos.map{ it.toString() }

            val id = transactionDb.insertTransaction(
                type = selectedTransactionType,
                amount = amount,
                currency = spinner.selectedItem.toString(),
                tag = selectedTag!!,
                date = dateText.text.toString(),
                comments = commentsEditText.text.toString(),
                photoUris = photoUriStrings
            )

            if (id > 0) {
                Toast.makeText(this, "Transaction saved", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}