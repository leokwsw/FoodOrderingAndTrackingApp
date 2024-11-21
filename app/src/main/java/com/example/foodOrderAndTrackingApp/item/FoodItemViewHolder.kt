package com.example.foodOrderAndTrackingApp.item

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foodOrderAndTrackingApp.R
import com.example.foodOrderAndTrackingApp.activity.CreateFoodActivity
import com.example.foodOrderAndTrackingApp.activity.RegisterActivity
import com.example.foodOrderAndTrackingApp.module.Food
import com.google.android.material.button.MaterialButton

class FoodItemViewHolder(
    private val context: Context, itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private var count: Int = 0
    private lateinit var food: Food

    fun onBindData(data: Food) {
        food = data

        itemView.findViewById<AppCompatTextView>(R.id.tv_title).apply {
            text = food.name
        }

        updateLayout()

        Glide
            .with(context)
            .load(food.image)
            .centerInside()
            .into(itemView.findViewById<AppCompatImageView>(R.id.img_img))

        itemView.findViewById<MaterialButton>(R.id.btn_add).apply {
            setOnClickListener {
                count++
                updateLayout()
            }
        }
        itemView.findViewById<MaterialButton>(R.id.btn_remove).apply {
            setOnClickListener {
                count--
                updateLayout()
            }
        }

        var popupMenu = PopupMenu(context, itemView)

        popupMenu.inflate(
            R.menu.item,
        )

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(context, CreateFoodActivity::class.java)
                    intent.putExtra(CreateFoodActivity.FOOD_ID, food.id)
                    context.startActivity(intent)
                    true
                }

                else -> true
            }
        }

        itemView.setOnLongClickListener {
            popupMenu.show()
            true
        }

    }

    private fun updateLayout() {
        itemView.findViewById<MaterialButton>(R.id.btn_add).apply {
            isEnabled = count < food.quota
        }
        itemView.findViewById<MaterialButton>(R.id.btn_remove).apply {
            isEnabled = count != 0
        }
        itemView.findViewById<AppCompatTextView>(R.id.tv_count).apply {
            text = String.format("%s", count)
        }
    }

}