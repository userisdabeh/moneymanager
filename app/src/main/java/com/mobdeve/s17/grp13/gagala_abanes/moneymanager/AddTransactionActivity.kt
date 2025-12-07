package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
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

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoUri: Uri

    fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val spinner: Spinner = findViewById(R.id.currencySpinner)
        val categories = resources.getStringArray(R.array.currency_options)

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

        val db = TagDatabase(this)
        val expenseTags = db.getAllExpenseTags()
        val incomeTags = db.getAllIncomeTags()

        var selectedButton: ImageButton? = null

        val gridLayout: GridLayout = findViewById(R.id.tagGrid)

        for (iconName in expenseTags) {
            val drawableId = resources.getIdentifier(iconName, "drawable", packageName)

            val button = ImageButton(this).apply {
                setImageResource(drawableId)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setPadding(16, 16, 16, 16)
                setBackgroundColor(Color.TRANSPARENT)
            }

            val params = GridLayout.LayoutParams().apply {
                width = 80.dpToPx()
                height = 80.dpToPx()
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.LEFT)
                setMargins(8, 8, 8, 8)
            }

            gridLayout.addView(button, params)

            button.setOnClickListener {
                selectedButton?.setBackgroundColor(Color.TRANSPARENT)

                button.setBackgroundColor(Color.LTGRAY)
                selectedButton = button

                val selectedIcon = iconName
            }
        }

        val dateInput: ImageButton = findViewById(R.id.dateSelector)
        val dateText: TextView = findViewById(R.id.selectedDate)

        val calendar = java.util.Calendar.getInstance()
        val todayString = "${calendar.get(java.util.Calendar.MONTH) + 1}/${calendar.get(java.util.Calendar.DAY_OF_MONTH)}/${calendar.get(java.util.Calendar.YEAR)}"
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


    }

    private fun showPhotoOptions() {
        val options = arrayOf("Camera", "Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setItems(options) {
            dialog, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
        }

        builder.show()
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
}