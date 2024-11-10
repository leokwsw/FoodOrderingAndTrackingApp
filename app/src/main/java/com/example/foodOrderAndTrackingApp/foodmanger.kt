package com.example.foodOrderAndTrackingApp

class Foodmanager {
    private val foodList: MutableList<CreateFood.Food> = mutableListOf() 

    fun addFood(name: String, category: String, price: Double, quota: Int?) {
        val food = CreateFood.Food(name, category, price, quota) 
        foodList.add(food)
    }

    fun getAllFoods(): List<CreateFood.Food> { 
        return foodList
    }
}
