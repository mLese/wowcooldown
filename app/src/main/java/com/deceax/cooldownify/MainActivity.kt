package com.deceax.cooldownify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.deceax.cooldownify.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "cooldownify"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences = getSharedPreferences("cooldownify", MODE_PRIVATE)
        val time = sharedPreferences.getLong("spellcloth", -1)

        if (time != -1L) {
            val timeSinceNotification = System.currentTimeMillis() - time
            val totalLength = (92 * 60 * 60 * 1000).toLong()
            val timeLeft = totalLength - timeSinceNotification
            binding.cooldownTimer.text = DateUtils.formatElapsedTime(timeLeft / 1000)
        } else {
            binding.spellclothIcon.setOnClickListener { spellclothClicked() }
        }
    }

    private fun spellclothClicked() {
        val sharedPreferences = getSharedPreferences("cooldownify", MODE_PRIVATE)
        val time = sharedPreferences.getLong("spellcloth", -1)

        if (time == -1L) {
            // create a notification 4 days from now
            val notificationWork: OneTimeWorkRequest =
                OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInitialDelay(92, TimeUnit.HOURS)
                    .build()
            WorkManager.getInstance(this).enqueue(notificationWork);
            Toast.makeText(this, "Notification Set", Toast.LENGTH_SHORT).show()

            sharedPreferences.edit().putLong("spellcloth", System.currentTimeMillis()).apply()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}