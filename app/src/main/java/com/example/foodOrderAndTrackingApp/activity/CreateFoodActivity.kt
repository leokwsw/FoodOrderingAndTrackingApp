package com.example.foodOrderAndTrackingApp.activity

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.drawable.Drawable
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
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foodOrderAndTrackingApp.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID


class CreateFoodActivity : AppCompatActivity() {
    private lateinit var edtName: AppCompatEditText
    private lateinit var edtPrice: AppCompatEditText
    private lateinit var edtQuota: AppCompatEditText
    private lateinit var availableSwitch: SwitchCompat
    private lateinit var btnAddFood: MaterialButton

    private lateinit var foodImageView: AppCompatImageView
    private var foodImageUrl: String = ""

    private lateinit var db: FirebaseFirestore

    private var isNewFood = true
    private var foodId = ""

    companion object {
        const val FOOD_ID = "food_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_food)

        db = Firebase.firestore

        edtName = findViewById(R.id.food_name)
        edtPrice = findViewById(R.id.food_price)
        edtQuota = findViewById(R.id.quota)
        availableSwitch = findViewById(R.id.available_switch)
        btnAddFood = findViewById(R.id.create_food_button)
        foodImageView = findViewById(R.id.image_preview)

        intent.extras?.let {
            if (it.containsKey(FOOD_ID) && it.getString(FOOD_ID) != null) {

                foodId = it.getString(FOOD_ID)!!

                db.collection("food").document(foodId).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            Log.d("RunTime", "${task.result.data}")
                            if (!task.result.data.isNullOrEmpty()) {
                                val data = task.result.data!!
                                edtName.setText(data["name"].toString())
                                val price = data["price"].toString().toFloatOrNull() ?: 0.0
                                edtPrice.setText(String.format("%s", price))
                                val quota = data["quota"].toString().toIntOrNull() ?: 0
                                edtQuota.setText(String.format("%s", quota))

                                foodImageUrl = data["image"].toString()
                                Glide
                                    .with(this)
                                    .load(foodImageUrl)
                                    .centerInside()
                                    .into(foodImageView)

                                val isAvailable = data["isAvailable"].toString() == "true"
                                availableSwitch.isChecked = isAvailable

                                findViewById<AppCompatTextView>(R.id.tv_title).text = "Edit Food Item"
                                btnAddFood.text = "Edit Food"
                            }

                        }
                    }

                isNewFood = false
            } else {
                isNewFood = true
            }
        }

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
                        foodImageUrl = downloadUrl.toString()
                        Glide
                            .with(this@CreateFoodActivity)
                            .load(foodImageUrl)
                            .centerInside()
                            .listener(object : RequestListener<Drawable> {
                                override fun onResourceReady(
                                    resource: Drawable,
                                    model: Any,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    pd.dismiss()
                                    return false
                                }

                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    pd.dismiss()
                                    return false
                                }
                            })
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
        val price = edtPrice.text.toString().toDoubleOrNull()
        val quota = edtQuota.text.toString().toIntOrNull()
        val isAvailable = availableSwitch.isChecked

        if (name.isNotEmpty() && price != null) {
            if (isNewFood) {
                db.collection("food").add(
                    hashMapOf(
                        "name" to name,
                        "price" to price,
                        "quota" to quota,
                        "image" to foodImageUrl,
                        "is_available" to isAvailable
                    )
                ).addOnSuccessListener {
                    Toast.makeText(this, "Food added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Please input valid food information", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                db.collection("food").document(foodId).set(
                    hashMapOf(
                        "name" to name,
                        "price" to price,
                        "quota" to quota,
                        "image" to foodImageUrl,
                        "is_available" to isAvailable
                    )
                ).addOnSuccessListener {
                    Toast.makeText(this, "Food edited successfully!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Please input valid food information", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(this, "Please input valid food information", Toast.LENGTH_SHORT).show()
        }
    }
}
