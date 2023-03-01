package com.example.inroad.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.inroad.R
import com.example.inroad.data.CarChoiceData
import com.example.inroad.managers.CarChoiceAdapter

class CarChoiceActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var carChoiceAdapter: CarChoiceAdapter
    lateinit var carChoiceList: ArrayList<CarChoiceData>

    lateinit var choiceBtn: Button

    var mCurrentFrame: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_choice_modal)

        viewPager = findViewById(R.id.idCarViewPager)
        choiceBtn = findViewById(R.id.idBtnChoice)


        choiceBtn.setOnClickListener() {
            startActivity(Intent(this@CarChoiceActivity, MainActivity::class.java))
        }

        carChoiceList = ArrayList()

        carChoiceList.add(
            CarChoiceData(
                R.drawable.vechical_car
            )
        )
        carChoiceList.add(
            CarChoiceData(
                R.drawable.hatchback_car
            )
        )
        carChoiceList.add(
            CarChoiceData(
                R.drawable.jeep_car
            )
        )
        carChoiceList.add(
            CarChoiceData(
                R.drawable.watcher_car
            )
        )

        carChoiceAdapter = CarChoiceAdapter(this, carChoiceList)

        viewPager.adapter = carChoiceAdapter

        viewPager.addOnPageChangeListener(viewListener)
    }

    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {

            mCurrentFrame = position

            if (position == 0) {
                choiceBtn.isEnabled = true

            } else if (position == 1) {
                choiceBtn.isEnabled = true
            } else {
                choiceBtn.isEnabled = true
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}