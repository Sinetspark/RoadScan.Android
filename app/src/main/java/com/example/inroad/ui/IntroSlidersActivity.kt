package com.example.inroad.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.inroad.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class IntroSlidersActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var dotsIndicator: DotsIndicator

    lateinit var sliderAdapter: SliderAdapter
    lateinit var sliderList: ArrayList<SliderData>

    lateinit var skipBtn: Button

    var mCurrentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_sliders)

        dotsIndicator = findViewById(R.id.dots_indicator)
        viewPager = findViewById(R.id.idViewPager)
        skipBtn = findViewById(R.id.idBtnSkip)

        skipBtn.setOnClickListener() {
            val i = Intent(this@IntroSlidersActivity, CarChoiceActivity::class.java)

            if (mCurrentPage == 2) {
                startActivity(i)
            }else {
                viewPager.currentItem = mCurrentPage + 1
            }
        }

        sliderList = ArrayList()

        sliderList.add(
            SliderData(
                "InRoad — это приложение, фиксирующее неровности на дорогах",
                R.drawable.road_intro_1
            )
        )

        sliderList.add(
            SliderData(
                "Приложение фиксирует неровности на дорогах с использованием данных акселерометра смартфона и привязкой к геолокации",
                R.drawable.road_intro_2
            )
        )

        sliderList.add(
            SliderData(
                "Для корректной работы приложения разрешите доступ к использованию данных о геолокации и акселерометра",
                R.drawable.road_intro_3
            )
        )


        sliderAdapter = SliderAdapter(this, sliderList)

        viewPager.adapter = sliderAdapter

        viewPager.addOnPageChangeListener(viewListener)

        dotsIndicator.attachTo(viewPager)

    }

    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {

            mCurrentPage = position

            if (position == 0) {
                skipBtn.isEnabled = true
                skipBtn.text = "Дальше"

            } else if (position == 1) {
                skipBtn.isEnabled = true
                skipBtn.text = "Дальше"
            } else {
                skipBtn.isEnabled = true
                skipBtn.text = "Разрешить"
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}