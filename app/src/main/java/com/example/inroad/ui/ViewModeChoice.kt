package com.example.inroad.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.inroad.R

class ViewModeChoice : AppCompatActivity() {

    lateinit var choiceBtn: Button
    lateinit var watcherMode: Button
    lateinit var driverMode: Button
    var selectedMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_mode_choice)

        choiceBtn = findViewById(R.id.idBtnChoiceMode)
        watcherMode = findViewById(R.id.watcher_mode)
        driverMode = findViewById(R.id.driver_mode)

        watcherMode.setOnClickListener {
            selectedMode = "watcherMode"
            watcherMode.setTextColor(ContextCompat.getColor(this, R.color.blue))
            driverMode.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        driverMode.setOnClickListener {
            selectedMode = "driverMode"
            driverMode.setTextColor(ContextCompat.getColor(this, R.color.blue))
            watcherMode.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        choiceBtn.setOnClickListener() {
            if (!selectedMode.isNullOrEmpty()) {
                val intent = Intent(this, CarChoiceActivity::class.java)
                intent.putExtra("selectedCar", selectedMode)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Выберите тип просмотра", Toast.LENGTH_SHORT).show()
            }

        }
    }
}