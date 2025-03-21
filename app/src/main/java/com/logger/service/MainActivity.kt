package com.logger.service

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get reference to the service start button
        val btnService = findViewById<Button>(R.id.btnService)
        btnService.setOnClickListener {
            // Check if notification access permission is granted
            if (!isNotificationAccessGranted()) {
                Toast.makeText(this, "Please grant Notification Access", Toast.LENGTH_LONG).show()
                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            } else {
                // Optionally, you can also check for Accessibility permission or Shizuku integration here
                Toast.makeText(this, "Service is running", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Check if the notification listener permission is enabled
    private fun isNotificationAccessGranted(): Boolean {
        val packageNames = NotificationListener.getEnabledListenerPackages(this)
        return packageNames.contains(packageName)
    }
}
