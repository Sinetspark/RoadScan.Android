package com.example.inroad.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.inroad.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        /*val reviewButton = findPreference<Preference>(getString(R.string.pref_key_review))

        reviewButton?.setOnPreferenceClickListener {
            Toast.makeText(context, "Pressed", Toast.LENGTH_SHORT).show()
        }*/
    }
}