package com.example.foodOrderAndTrackingApp

import android.os.Bundle
import andorid.widget.Button
import android.widget.EditText
import andorid.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateFood : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtCategory: EditText
    private lateinit var edtPrice: EditText
    private lateinit var btnAddFood: Button
    private val food = Food()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_food)

        edtName = findViewById(R.id.edt_name)
        edtCategory = findViewById(R.id.edt_category)
        edtCategory = findViewById(R.id.edt_price)
        edtCategory = findViewById(R.id.btn_add_price)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnAddFood.setOnClickListener{
            addFood
        }
    }
    private fun addFood(){
        val name= edtName.text.toString()
        val category = edtCategory.text.toString()
        val price = edt[rice.text.toString().toDoubleOrNull()

        if(name.isNotEmpty() && category.isNotEmpty() && price != null) {
            food.addFood(name, category, price)
            Toast.makeText(this,"food add successful",Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(this,"plase input food",LENGTH_SHORT).show()
        }
    }
}
