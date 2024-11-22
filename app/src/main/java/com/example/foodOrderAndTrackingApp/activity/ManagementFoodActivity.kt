package com.example.foodOrderAndTrackingApp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodOrderAndTrackingApp.adapter.FoodListAdapter
import com.example.foodOrderAndTrackingApp.R
import com.example.foodOrderAndTrackingApp.module.Food
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

class ManagementFoodActivity : AppCompatActivity() {

    private var foodList: ArrayList<Food> = arrayListOf()
    private lateinit var db: FirebaseFirestore
    private lateinit var foodListAdapter: FoodListAdapter

    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.manage_food_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = Firebase.firestore

        findViewById<MaterialButton>(R.id.btn_logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java).apply {
                this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val size = foodList.size
                    foodList.clear()
                    foodListAdapter.notifyItemRangeChanged(0, size)
                    getDatabaseData()
                }
            }

        findViewById<MaterialButton>(R.id.add_food_button).setOnClickListener {
            activityResultLauncher.launch(Intent(this, CreateFoodActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.delivery_tracking_button).setOnClickListener {
            startActivity(Intent(this, DeliveryTrackingActivity::class.java))
        }

        foodListAdapter = FoodListAdapter(this, foodList, activityResultLauncher)

        findViewById<RecyclerView>(R.id.rv_food_items_list).apply {
            val layoutManager = GridLayoutManager(this@ManagementFoodActivity, 2)
            this.layoutManager = layoutManager

            getDatabaseData()

            this.adapter = foodListAdapter

        }

        val uid = Firebase.auth.uid
        if (!uid.isNullOrEmpty()) {
            Log.d("RunTime", "user/${uid}")
            db.collection("user").document(uid).get().addOnSuccessListener { docs ->
                if (docs != null) {
                    val role = docs.data!!["role"]
                    isAdmin = role == "Admin"

                    findViewById<MaterialButton>(R.id.add_food_button).visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getDatabaseData() {
        db.collection("food").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val docs: QuerySnapshot = task.result

                    docs.documents.forEachIndexed { index, document ->
                        if (!document.data.isNullOrEmpty()) {
                            val data = document.data!!
                            val food = Food(
                                document.id,
                                data["name"].toString(),
                                data["price"].toString().toFloatOrNull() ?: 0.0,
                                data["quota"].toString().toIntOrNull() ?: 0,
                                data["image"].toString(),
                                data["isAvailable"].toString() == "true",
                            )
                            foodList.add(food)
                            foodListAdapter.notifyItemChanged(index)
                        }
                    }
                } else {
                    Toast
                        .makeText(
                            this@ManagementFoodActivity,
                            "No Food",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }
    }
}