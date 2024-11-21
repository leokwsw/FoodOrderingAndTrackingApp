package com.example.foodOrderAndTrackingApp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.foodOrderAndTrackingApp.R
import com.example.foodOrderAndTrackingApp.item.FoodItemViewHolder
import com.example.foodOrderAndTrackingApp.module.Food

class FoodListAdapter(
    private val context: Context,
    private val data: ArrayList<Food>,
    private val activityResultLauncher: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<FoodItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder(
            context,
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_food,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        Log.d("RV", "size : ${data.size}")
        return data.size
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        holder.onBindData(data[position], activityResultLauncher)
    }

}