package com.example.foodOrderAndTrackingApp.item

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodOrderAndTrackingApp.R
import com.example.foodOrderAndTrackingApp.activity.CreateFoodActivity
import com.example.foodOrderAndTrackingApp.module.Food
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class FoodItemViewHolder(
    private val context: Context, itemView: View, private val removeAt: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private var count: Int = 0
    private lateinit var food: Food

    fun onBindData(
        position: Int,
        data: Food,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
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

        val uid = Firebase.auth.uid
        if (!uid.isNullOrEmpty()) {
            Firebase.firestore
                .collection("user")
                .document(uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.data != null) {
                            val data = task.result.data!!
                            val role = data["role"]
                            val isAdmin = role == "Admin"

                            if (isAdmin) {
                                val popupMenu = PopupMenu(context, itemView)

                                popupMenu.inflate(
                                    R.menu.item,
                                )

                                popupMenu.setOnMenuItemClickListener {
                                    when (it.itemId) {
                                        R.id.menu_edit -> {
                                            val intent =
                                                Intent(context, CreateFoodActivity::class.java)
                                            intent.putExtra(CreateFoodActivity.FOOD_ID, food.id)
                                            activityResultLauncher.launch(intent)
                                            true
                                        }

                                        R.id.menu_delete -> {
                                            AlertDialog.Builder(context)
                                                .setTitle("Delete Food")
                                                .setMessage("Do you really want to delete Food?")
                                                .setPositiveButton(
                                                    "Yes"
                                                ) { _, _ ->
                                                    Firebase.firestore
                                                        .collection("food")
                                                        .document(food.id)
                                                        .delete()
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                removeAt.invoke(position)
                                                            }
                                                        }


                                                }
                                                .setNeutralButton(
                                                    "No"
                                                ) { _, _ ->

                                                }
                                                .show()
                                            true
                                        }

                                        else -> true
                                    }
                                }

                                itemView.setOnLongClickListener {
                                    popupMenu.show()
                                    true
                                }
                            } else {
                                Log.d("RunTime", "ViewHolder not Admin")
                            }
                        } else {
                            Log.d("RunTime", "ViewHolder exp : data null")
                        }
                    } else {
                        Log.d("RunTime", "ViewHolder exp : ${task.exception}")
                    }
                }
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