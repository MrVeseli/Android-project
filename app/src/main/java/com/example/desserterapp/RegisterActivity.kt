package com.example.desserterapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val currentWeightText = findViewById<EditText>(R.id.currentWeightNumber)
            val desiredWeightText = findViewById<EditText>(R.id.desiredWeightNumber)
            val daysText = findViewById<EditText>(R.id.daysIntervalNumber)

            val calorieGoal = calculateCalorieGoal(
                Integer.parseInt(currentWeightText.text.toString()),
                Integer.parseInt(desiredWeightText.text.toString()),
                Integer.parseInt(daysText.text.toString())
            )

            editor.apply {
                putInt("calorie_goal", calorieGoal).commit()
            }

            Toast.makeText(this, "Sucessfully registered", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java));
        }
    }

    fun calculateCalorieGoal(currentWeight: Int, desiredWeight: Int, days: Int): Int {
        val oneKgInCals = 7700
        val dailyIntake = 2500
        if (currentWeight > desiredWeight) { //weight loss
            var dailyDecrease = (currentWeight - desiredWeight) * oneKgInCals / days
            return (dailyIntake - dailyDecrease)
        }
        else if (currentWeight < desiredWeight) {
            var dailyIncrease = (desiredWeight - currentWeight) * oneKgInCals / days
            return (dailyIntake + dailyIncrease)
        }
        else return dailyIntake
    }
}