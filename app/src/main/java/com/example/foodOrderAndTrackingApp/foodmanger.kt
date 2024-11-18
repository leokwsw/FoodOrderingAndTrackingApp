package com.example.foodOrderAndTrackingApp

class Foodmanager {
    private val foodList: MutableList<CreateFoodActivity.Food> = mutableListOf()

    fun addFood(name: String, category: String, price: Double, quota: Int?) {
        val food = CreateFoodActivity.Food(name, category, price, quota)
        foodList.add(food)
    }

    fun getAllFoods(): List<CreateFoodActivity.Food> {
        return foodList
    }
}
