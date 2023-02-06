package com.example.desserterapp

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity


class RecipeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val frame = FrameLayout(this)
        if (savedInstanceState == null) {
            val fragment = RecipeDetailFragment()
            val toPass = Bundle()
            val bundle = intent.extras
            toPass.putString("title", bundle?.get("title").toString())
            toPass.putString("servings", bundle?.get("servings").toString())
            toPass.putString("instructions", bundle?.get("instructions").toString())

            fragment.arguments = intent.extras
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commit()
        }

    }
}