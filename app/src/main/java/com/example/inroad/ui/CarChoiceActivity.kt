package com.example.inroad.ui

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inroad.R


class CarChoiceActivity : AppCompatActivity() {

    lateinit var choiceBtn: Button
    lateinit var vechicalCarImg: ImageView
    lateinit var hatchbackCarImg: ImageView
    lateinit var jeepCarImg: ImageView
    lateinit var watcherCarImg: ImageView
    var selectedCar = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_choice_modal)

        choiceBtn = findViewById(R.id.idBtnChoice)
        vechicalCarImg = findViewById(R.id.vechical_car_img)
        hatchbackCarImg= findViewById(R.id.hatchback_car_img)
        jeepCarImg = findViewById(R.id.jeep_car_img)
        watcherCarImg = findViewById(R.id.watcher_car_img)

        vechicalCarImg.setOnClickListener {
            selectedCar = "vechical_car"
        }

        hatchbackCarImg.setOnClickListener {
            selectedCar = "hatchback_car"
        }

        jeepCarImg.setOnClickListener {
            selectedCar = "jeep_car"
        }

        watcherCarImg.setOnClickListener {
            selectedCar = "watcher"
        }

        choiceBtn.setOnClickListener() {
            if (!selectedCar.isNullOrEmpty()) {
                val intent = Intent(this@CarChoiceActivity, MainActivity::class.java)
                intent.putExtra("selectedCar", selectedCar)
                startActivity(intent)
            } else {
                Toast.makeText(this@CarChoiceActivity, "Выберите тип автомобиля", Toast.LENGTH_SHORT).show()
            }

        }
    }
}