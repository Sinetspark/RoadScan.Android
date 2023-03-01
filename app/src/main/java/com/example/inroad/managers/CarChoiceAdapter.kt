package com.example.inroad.managers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.inroad.R
import com.example.inroad.data.CarChoiceData

class CarChoiceAdapter (
    val context: Context,
    val carsList: ArrayList<CarChoiceData>
    ) : PagerAdapter() {

        override fun getCount(): Int {

            return carsList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {

            return view === `object` as RelativeLayout
        }

        override fun instantiateItem(container2: ViewGroup, position: Int): Any {

            val layoutInflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view: View = layoutInflater.inflate(R.layout.cars_item, container2, false)

            val imageView: ImageView = view.findViewById(R.id.carsImage)
            //val carsName: TextView = view.findViewById(R.id.carsName)

            val carsData: CarChoiceData = carsList.get(position)
            //carsName.text = carsData.carName
            imageView.setImageResource(carsData.carImage)

            container2.addView(view)

            return view
        }

        override fun destroyItem(container2: ViewGroup, position: Int, `object`: Any) {

            container2.removeView(`object` as RelativeLayout)
        }

}