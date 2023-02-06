package com.example.desserterapp.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface RecipeApiEndPoints {

    @Headers(
        "X-RapidAPI-Key: 445acc8618msh3ced67470c80fa8p14f3d5jsnf42718db063e",
        "X-RapidAPI-Host: recipe-by-api-ninjas.p.rapidapi.com"
    )
    @GET("recipe")
    fun getRecipes(
        @Query("query") query: String,
    ): Call<ArrayList<Recipe>>
}