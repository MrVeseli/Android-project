package com.example.desserterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desserterapp.api.Recipe
import com.example.desserterapp.api.RecipeApiEndPoints
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MealIdeasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_ideas)

        val request = ServiceBuilder.buildService(RecipeApiEndPoints::class.java)
        val findMealEditText = findViewById<EditText>(R.id.findMealEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            val call = request.getRecipes(findMealEditText.text.toString())
            call.enqueue(object : Callback<ArrayList<Recipe>> {
                override fun onResponse(call: Call<ArrayList<Recipe>>, response: Response<ArrayList<Recipe>>) {
                    if(response.isSuccessful) {
                        findViewById<RecyclerView>(R.id.mealRecyclerView).apply {
                            layoutManager = LinearLayoutManager(this@MealIdeasActivity)
                            adapter = RecipeRecyclerAdapter(response.body()!!)
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Recipe>>, t: Throwable) {
                    Log.d("LOADING_FAIL", t.message.toString())
                }
            })
        }
        //TODO tap na recept daje detalje o njemu
    }

    private fun initView() {
        findViewById<RecyclerView>(R.id.mealRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@MealIdeasActivity)
            adapter = RecipeRecyclerAdapter(ArrayList<Recipe>())
        }
    }
}

class RecipeRecyclerAdapter(private val items: List<Recipe>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.meal, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is RecipeViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //

    class RecipeViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        private val recipeName: TextView = itemView.findViewById(R.id.nameTextView)
        private val recipeServings: TextView = itemView.findViewById(R.id.servingsTextView)

        fun bind(recipe: Recipe) {
            recipeName.text = recipe.title
            recipeServings.text = recipe.servings
        }
    }
}

object ServiceBuilder {
    private val client = OkHttpClient()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipe-by-api-ninjas.p.rapidapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}