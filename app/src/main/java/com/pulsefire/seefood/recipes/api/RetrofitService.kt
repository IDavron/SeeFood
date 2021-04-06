package com.pulsefire.seefood.recipes.api

import com.pulsefire.seefood.recipes.api.data.Recipe
import com.pulsefire.seefood.recipes.api.data.RecipeInfo
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @Headers(
            "x-rapidapi-key: 3AkpyfWrLAmshf9BUTe0Ts3Bf50jp1KsOOjjsnjWK7NZ8UduUB",
            "x-rapidapi-host: spoonacular-recipe-food-nutrition-v1.p.rapidapi.com",
            "useQueryString: true"
    )
    @GET("recipes/{id}/information")
    fun getRecipeById(@Path("id") id: Int) : Call<RecipeInfo>

    @Headers(
            "x-rapidapi-key: 3AkpyfWrLAmshf9BUTe0Ts3Bf50jp1KsOOjjsnjWK7NZ8UduUB",
            "x-rapidapi-host: spoonacular-recipe-food-nutrition-v1.p.rapidapi.com",
            "useQueryString: true"
    )
    @GET("recipes/findByIngredients")
    fun getRecipesByIngredients(
            @Query("number") number: Int,
            @Query("ranking") ranking: Int,
            @Query("ingredients") ingredients: String,
            @Query("ignorePantry") ignorePantry: Boolean
    ) : Call<MutableList<Recipe>>
}