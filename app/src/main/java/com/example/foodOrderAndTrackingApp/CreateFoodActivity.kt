package com.example.foodOrderAndTrackingApp

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID


class CreateFoodActivity : AppCompatActivity() {
    private lateinit var edtName: AppCompatEditText
    private lateinit var edtCategory: AppCompatEditText
    private lateinit var edtPrice: AppCompatEditText
    private lateinit var edtQuota: AppCompatEditText
    private lateinit var availableSwitch: SwitchCompat
    private lateinit var btnAddFood: MaterialButton

    private lateinit var foodImageView: AppCompatImageView
    private var foodImageUrl: String = ""

    private lateinit var db: FirebaseFirestore

    data class Food(
        val name: String, val category: String, val price: Double, val quota: Int?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_food)

        db = Firebase.firestore

        edtName = findViewById(R.id.food_name)
        edtCategory = findViewById(R.id.food_description)
        edtPrice = findViewById(R.id.food_price)
        edtQuota = findViewById(R.id.quota)
        availableSwitch = findViewById(R.id.available_switch)
        btnAddFood = findViewById(R.id.create_food_button)
        foodImageView = findViewById(R.id.image_preview)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialButton>(R.id.btn_back).setOnClickListener {
            finish()
        }


        btnAddFood.setOnClickListener {
            addFood()
        }

        setUpImagePicker()
    }

    private fun setUpImagePicker() {
        val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("PhotoPicker", "Number of items selected: $uri")
                    uploadImage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        findViewById<MaterialButton>(R.id.select_image_button).setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        return contentResolver.getType(uri)?.let { mimeType ->
            // Extract the extension from the MIME type
            MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        } ?: run {
            // Alternatively, try extracting the extension from the file name
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    cursor.moveToFirst()
                    cursor.getString(nameIndex)?.substringAfterLast('.', "")
                } else null
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imageRef =
            storageRef.child("images/${UUID.randomUUID()}.${getFileExtension(imageUri) ?: "jpg"}")

        val pd = ProgressDialog(this).apply {
            setTitle("Uploading")
            setMessage("Please wait while the image is being uploaded...")
            setCancelable(false)
        }

        pd.show()

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef
                    .downloadUrl.addOnSuccessListener { downloadUrl ->
                        pd.dismiss()
                        foodImageUrl = downloadUrl.toString()
                        Glide
                            .with(this@CreateFoodActivity)
                            .load(foodImageUrl)
                            .centerCrop()
                            .into(foodImageView)
                    }
            }
            .addOnFailureListener { exception ->
                pd.dismiss()
                Toast.makeText(this, "Upload failed: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun addFood() {
        val name = edtName.text.toString()
        val category = edtCategory.text.toString()
        val price = edtPrice.text.toString().toDoubleOrNull()
        val quota = edtQuota.text.toString().toIntOrNull()
        val isAvailable = availableSwitch.isChecked

        if (name.isNotEmpty() && category.isNotEmpty() && price != null) {
//            db.collection("food").document("AAAAa").set("")

            db.collection("food").add(
                hashMapOf(
                    "name" to name,
                    "category" to category,
                    "price" to price,
                    "quota" to quota,
                    "image" to foodImageUrl,
                    "is_available" to isAvailable
                )
            ).addOnSuccessListener {
//            val food = Food(name, category, price, quota)
                Toast.makeText(this, "Food added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Please input valid food information", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "Please input valid food information", Toast.LENGTH_SHORT).show()
        }
    }
}
