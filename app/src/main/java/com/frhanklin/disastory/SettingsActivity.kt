package com.frhanklin.disastory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.frhanklin.disastory.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(preferences)).get(
            SettingsViewModel::class.java
        )
        settingsViewModel.getThemeSettings().observe(this) { nightStateOn: Boolean ->
            if (nightStateOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchNight.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchNight.isChecked = false
            }
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.switchNight.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSettings(isChecked)
        }

    }
}