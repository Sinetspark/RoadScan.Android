package com.example.inroad.ui

import android.R.attr.button
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.inroad.R


class CarChoiceActivity : AppCompatActivity() {

    lateinit var choiceBtn: Button
    lateinit var vechicalCarCard: CardView
    lateinit var hatchbackCarCard: CardView
    lateinit var jeepCarCard: CardView
    lateinit var watcherCarCard: CardView

    lateinit var vechicalCarText: TextView
    lateinit var hatchbackCarText: TextView
    lateinit var jeepCarText: TextView
    lateinit var watcherCarText: TextView

    lateinit var vechicalCarImg: ImageView
    lateinit var hatchbackCarImg: ImageView
    lateinit var jeepCarImg: ImageView


    var selectedCar = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_choice_modal)

        choiceBtn = findViewById(R.id.idBtnChoice)
        vechicalCarCard = findViewById(R.id.vechical_car)
        hatchbackCarCard = findViewById(R.id.hatchback_car)
        jeepCarCard = findViewById(R.id.jeep_car)
        watcherCarCard = findViewById(R.id.watcher_car_img)

        vechicalCarText = findViewById(R.id.vechical_car_text)
        hatchbackCarText = findViewById(R.id.hatchback_car_text)
        jeepCarText = findViewById(R.id.jeep_car_text)
        watcherCarText = findViewById(R.id.watcher_car_text)

        vechicalCarImg = findViewById(R.id.vechical_car_img)
        jeepCarImg = findViewById(R.id.jeep_car_img)
        hatchbackCarImg = findViewById(R.id.hatchback_car_img)

        val colorBLue = ContextCompat.getColor(this, R.color.blue)
        val colorBLack = ContextCompat.getColor(this, R.color.black)

        val carCards = listOf(
            vechicalCarCard to "vechical_car",
            hatchbackCarCard to "hatchback_car",
            jeepCarCard to "jeep_car",
            watcherCarCard to "watcher"
        )

        val carTexts = listOf(
            vechicalCarText,
            hatchbackCarText,
            jeepCarText,
            watcherCarText
        )

        val carImgs = listOf(
            vechicalCarImg,
            hatchbackCarImg,
            jeepCarImg
        )

        for ((card, carType) in carCards) {
            card.setOnClickListener {
                selectedCar = carType
                carTexts.forEachIndexed { index, carText ->
                    val isSelected = index == carCards.indexOfFirst { it.second == carType }
                    carText.setTextColor(if (isSelected) colorBLue else colorBLack)
                    carText.alpha = if (isSelected) 1f else 0.7f
                }
                carImgs.forEachIndexed { index, carImg ->
                    val isSelected = index == carCards.indexOfFirst { it.second == carType }
                    carImg.alpha = if (isSelected) 1f else 0.7f
                }
            }

            choiceBtn.setOnClickListener() {
                if (!selectedCar.isNullOrEmpty()) {
                    val intent = Intent(this@CarChoiceActivity, MainActivity::class.java)
                    intent.putExtra("selectedCar", selectedCar)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@CarChoiceActivity,
                        "Выберите тип автомобиля",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}