package com.example.foodOrderAndTrackingApp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateFood : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtCategory: EditText
    private lateinit var edtPrice: EditText
    private lateinit var edtQuota: EditText     
    private lateinit var availableSwitch: Switch   
    private lateinit var btnAddFood: Button

    data class Food(
        val name: String,
        val category: String,
        val price: Double,
        val quota: Int?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_food)

     
        edtName = findViewById(R.id.food_name)
        edtCategory = findViewById(R.id.food_description)
        edtPrice = findViewById(R.id.food_price)
        edtQuota = findViewById(R.id.quota)              
        availableSwitch = findViewById(R.id.available_switch) 
        btnAddFood = findViewById(R.id.create_food_button)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

      
        btnAddFood.setOnClickListener {
            addFood()
        }
    }

    private fun addFood() {
        val name = edtName.text.toString()
        val category = edtCategory.text.toString()
        val price = edtPrice.text.toString().toDoubleOrNull()
        val quota = edtQuota.text.toString().toIntOrNull() 
        val isAvailable = availableSwitch.isChecked

 
        if (name.isNotEmpty() && category.isNotEmpty() && price != null) {
            val food = Food(name, category, price, quota) 
            Toast.makeText(this, "Food added successfully!", Toast.LENGTH_SHORT).show()
            finish() 
        } else {
            Toast.makeText(this, "Please input valid food information", Toast.LENGTH_SHORT).show() 
        }
    }
}
