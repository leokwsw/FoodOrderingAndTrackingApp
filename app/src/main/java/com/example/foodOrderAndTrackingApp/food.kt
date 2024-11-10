package com.example.foodOrderAndTrackingApp

class Food{
  private val foodList: MutableList<Food> = mutableListOf()

  fun addFood(name:String, category:String, price:int){
    var food = Food(name, category, price)
    foodList.add(food)
  }
fun getAllFoods(); List<food> {
  return foodList
  }
}
