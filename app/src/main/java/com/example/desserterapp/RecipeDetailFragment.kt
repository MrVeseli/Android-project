package com.example.desserterapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecipeDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        val titleTextView = view.findViewById<TextView>(R.id.fragTitleTextView)
        val servingsTextView = view.findViewById<TextView>(R.id.fragSevingsTextView)
        val instructionsTextView = view.findViewById<TextView>(R.id.fragInstructionsTextView)

        titleTextView.text = arguments?.get("title").toString()
        servingsTextView.text = arguments?.get("servings").toString()
        instructionsTextView.text = arguments?.get("instructions").toString()

        return view
    }
}