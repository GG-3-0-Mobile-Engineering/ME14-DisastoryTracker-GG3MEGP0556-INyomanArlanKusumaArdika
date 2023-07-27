package com.frhanklin.disastory.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.frhanklin.disastory.R
import com.frhanklin.disastory.utils.SettingPreferences
import com.frhanklin.disastory.presentation.viewmodel.SettingsViewModel
import com.frhanklin.disastory.utils.ViewModelFactory
import com.frhanklin.disastory.utils.dataStore
import com.frhanklin.disastory.databinding.ActivitySettingsBinding
import com.frhanklin.disastory.utils.AndroidResourceProvider
import com.frhanklin.disastory.utils.PermissionHandler
import com.frhanklin.disastory.utils.ResourceProvider

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var pref: SettingPreferences
    private lateinit var rp: ResourceProvider
    private lateinit var permissionHandler: PermissionHandler



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
        permissionHandler = PermissionHandler(this)


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

//                if (Build.VERSION.SDK_INT >= 33) {
//                    permissionHandler.requestPermission(Manifest.permission.POST_NOTIFICATIONS, object: PermissionHandler.PermissionListener {
//                        override fun onPermissionGranted() {
//                            settingsViewModel.saveNotificationSettings(true)
////                            Toast.makeText(this@SettingsActivity, R.string.notification_enabled, Toast.LENGTH_SHORT).show()
//                        }
//
//                        override fun onPermissionDenied() {
//                            settingsViewModel.saveNotificationSettings(false)
//                            binding.switchNotification.isChecked = false
////                            Toast.makeText(this@SettingsActivity, R.string.permission_denied, Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                } else {
//                    settingsViewModel.saveNotificationSettings(true)
//                    binding.switchNotification.isChecked = true
////                    Toast.makeText(this@SettingsActivity, R.string.notification_enabled, Toast.LENGTH_SHORT).show()
//                }
            } else {
                settingsViewModel.saveNotificationSettings(false)
                binding.switchNotification.isChecked = false
                Toast.makeText(this@SettingsActivity, R.string.notification_disabled, Toast.LENGTH_SHORT).show()
            }
        }

    }
}