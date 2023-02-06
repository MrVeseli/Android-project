package com.example.desserterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RecipeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val servingsTextView = findViewById<TextView>(R.id.detailServingsTextView)
        val instructionsTextView = findViewById<TextView>(R.id.instructionsTextView)

        val bundle = intent.extras
        titleTextView.text = bundle?.get("title").toString()
        servingsTextView.text = bundle?.get("servings").toString()
        instructionsTextView.text = "Instructions: " + bundle?.get("instructions").toString()
    }
}