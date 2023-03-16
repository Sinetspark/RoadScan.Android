package com.example.inroad.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.inroad.R

class SettingsActivity : AppCompatActivity() {

    lateinit var reviewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_settings)

        reviewButton = findViewById(R.id.btnReview)

        //not working
        reviewButton.setOnClickListener(){
            Toast.makeText(this, "Thanks", Toast.LENGTH_SHORT).show()
        }
    }
}

