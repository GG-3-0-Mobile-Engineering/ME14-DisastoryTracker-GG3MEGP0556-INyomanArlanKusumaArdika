package com.frhanklin.disastory.presentation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.frhanklin.disastory.R
import com.frhanklin.disastory.presentation.ui.viewmodel.SettingsViewModel
import com.frhanklin.disastory.databinding.ActivitySettingsBinding
import com.frhanklin.disastory.utils.PermissionHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var permissionHandler: PermissionHandler

    private val settingsViewModel : SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionHandler = PermissionHandler(this)

        settingsViewModel.getThemeSettings().observe(this) { nightStateOn: Boolean ->
            if (nightStateOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchNight.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchNight.isChecked = false
            }
        }
        settingsViewModel.getNotificationSettings().observe(this) { isEnabled: Boolean ->
            binding.switchNotification.isChecked = isEnabled
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.switchNight.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSettings(isChecked)
        }
        binding.switchNotification.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                settingsViewModel.saveNotificationSettings(true)
            } else {
                settingsViewModel.saveNotificationSettings(false)
                binding.switchNotification.isChecked = false
                Toast.makeText(this@SettingsActivity, R.string.notification_disabled, Toast.LENGTH_SHORT).show()
            }
        }

    }
}