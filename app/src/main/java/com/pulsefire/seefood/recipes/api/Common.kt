package com.pulsefire.seefood.recipes.api

object Common {
    private const val BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/"
    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}