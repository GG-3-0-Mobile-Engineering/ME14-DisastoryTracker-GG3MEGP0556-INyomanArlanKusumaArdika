package com.frhanklin.disastory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.frhanklin.disastory.databinding.ActivitySettingsBinding
import com.frhanklin.disastory.utils.AndroidResourceProvider
import com.frhanklin.disastory.utils.ResourceProvider

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var pref: SettingPreferences
    private lateinit var rp: ResourceProvider


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SettingPreferences.getInstance(application.dataStore)
        rp = AndroidResourceProvider(applicationContext)

        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref, rp)).get(
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