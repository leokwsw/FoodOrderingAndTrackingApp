package com.example.foodOrderAndTrackingApp

class Food(
    val name: String,
    val category: String,
    val price: Int
) {
    private val foodList: MutableList<Food> = mutableListOf()

    fun addFood(name: String, category: String, price: Int) {
        val food = Food(name, category, price)
        foodList.add(food)
    }

    fun getAllFoods(): List<Food> {  
        return foodList
    }
}
