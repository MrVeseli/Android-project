package com.example.desserterapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Check if it has to show register options
        var isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            startActivity(Intent(this, RegisterActivity::class.java));
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).apply();

        // Data setup on load
        var currentDailyCalories = sharedPreferences.getInt("current_daily_calories", 0)
        if (isNewDay()) {
           currentDailyCalories = 0
        }

        // Getting all input components
        updateOutput(currentDailyCalories)
        editor.apply {
            putInt("current_daily_calories", currentDailyCalories).apply()
        }

        val nutritionInput = findViewById<EditText>(R.id.editNutritonNumber)
        val amountInput = findViewById<EditText>(R.id.editAmountNumber)
        val addMealButton = findViewById<Button>(R.id.addMealButton)

        val workoutDurationInput = findViewById<EditText>(R.id.editDurationNumber)
        val addWorkoutButton = findViewById<Button>(R.id.addWorkoutButton)

        val suggestMealButton = findViewById<Button>(R.id.suggestMealButton)

        // Setting listeners for buttons
        addMealButton.setOnClickListener {
            try {
                currentDailyCalories += calculateMealCalories(
                    Integer.parseInt(nutritionInput.text.toString()),
                    Integer.parseInt(amountInput.text.toString())
                )
            }
            catch(e: Exception) {
                Log.e("ERROR_ADD","Error with adding calories.")
            }

            updateOutput(currentDailyCalories)
            editor.apply {
                putInt("current_daily_calories", currentDailyCalories).apply()
            }
        }

        addWorkoutButton.setOnClickListener {
            try {
            currentDailyCalories -= calculateWorkoutCalories(
                Integer.parseInt(workoutDurationInput.text.toString())
            ) }
            catch(e: Exception) {
                Log.e("ERROR_SUB","Error with subtracting calories.")
            }
            updateOutput(currentDailyCalories)
            editor.apply {
                putInt("current_daily_calories", currentDailyCalories).apply()
            }
        }

        suggestMealButton.setOnClickListener {
            startActivity(Intent(this, MealIdeasActivity::class.java));
        }
    }

    override fun onDestroy() {
        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var dayLastClosed = LocalDate.now().dayOfYear
        editor.apply {
            putInt("day_last_open", dayLastClosed).apply()
        }
        super.onDestroy()
    }

    fun isNewDay(): Boolean {
        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        var currentDay = LocalDate.now().dayOfYear
        var dayLastClosed = sharedPreferences.getInt("day_last_open", 0)

        if (currentDay != dayLastClosed) {
            return true
        }
        return false
    }

    fun calculateMealCalories(nutrition: Int, amount: Int): Int {
        return (nutrition * amount * 0.01).toInt()
    }

    fun calculateWorkoutCalories(duration: Int): Int {
        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val weight = sharedPreferences.getInt("weight", 0)
        val MET = 3.5

        return (duration * MET * weight / 200).toInt()
    }

    fun updateOutput(currentDailyCalories: Int) {
        val sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val dailyCaloriesCounter = findViewById<TextView>(R.id.caloriesTextView)
        val messageTextView = findViewById<TextView>(R.id.messageTextView)

        var calorieGoal = sharedPreferences.getInt("calorie_goal", 0)
        var message = "Calorie goal: " + calorieGoal.toString()
        if(currentDailyCalories < 0) {
            message = "Working out more than you eat?"

        }
        else if ((calorieGoal - 150) < currentDailyCalories && currentDailyCalories < (calorieGoal + 150)){
            message = "Congratulations! You are meeting your goals!"
            //TODO notifikacija
        }
        dailyCaloriesCounter.text = currentDailyCalories.toString() + " kcal today"
        messageTextView.text = message
    }
}