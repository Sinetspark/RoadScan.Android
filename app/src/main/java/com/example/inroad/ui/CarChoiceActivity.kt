package com.example.inroad.ui

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.inroad.R


class CarChoiceActivity : AppCompatActivity() {

    lateinit var choiceBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_choice_modal)
        val vechicalCar = findViewById<ImageView>(R.id.vechical_car_img)

        choiceBtn = findViewById(R.id.idBtnChoice)
        vechicalCar.setOnClickListener(){
        }



        choiceBtn.setOnClickListener() {
            startActivity(Intent(this@CarChoiceActivity, MainActivity::class.java))
        }
    }
}