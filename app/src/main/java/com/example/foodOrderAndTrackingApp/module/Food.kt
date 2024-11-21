package com.example.foodOrderAndTrackingApp.module

data class Food(
    val id: String,
    val name: String,
    val price: Number,
    val quota: Int,
    val image: String,
    val isAvailable: Boolean
)
