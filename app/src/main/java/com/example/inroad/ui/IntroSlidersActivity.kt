package com.example.inroad.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.inroad.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class IntroSlidersActivity : AppCompatActivity() {

    // on below line we are creating a
    // variable for our view pager
    lateinit var viewPager: ViewPager
    lateinit var dotsIndicator: DotsIndicator

    // on below line we are creating a variable
    // for our slider adapter and slider list
    lateinit var sliderAdapter: SliderAdapter
    lateinit var sliderList: ArrayList<SliderData>

    // on below line we are creating a variable for our
    // skip button, slider indicator text view for 3 dots
    lateinit var skipBtn: Button
    /*lateinit var indicatorSlideOneTV: TextView
    lateinit var indicatorSlideTwoTV: TextView
    lateinit var indicatorSlideThreeTV: TextView*/

    var mCurrentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_sliders)

        dotsIndicator = findViewById(R.id.dots_indicator)
        viewPager = findViewById(R.id.idViewPager)
        skipBtn = findViewById(R.id.idBtnSkip)



        /*indicatorSlideOneTV = findViewById(R.id.idTVSlideOne)
        indicatorSlideTwoTV = findViewById(R.id.idTVSlideTwo)
        indicatorSlideThreeTV = findViewById(R.id.idTVSlideThree)*/

        skipBtn.setOnClickListener() {
            val i = Intent(this@IntroSlidersActivity, MainActivity::class.java)

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

                /*indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.white))*/

            } else if (position == 1) {
                skipBtn.isEnabled = true
                skipBtn.setText("Дальше")


                /*indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.white))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))*/
            } else {
                skipBtn.isEnabled = true
                skipBtn.setText("Разрешить")

                /*indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.white))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))*/
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}