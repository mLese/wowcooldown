package com.deceax.cooldownify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
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

        binding.spellclothIcon.setOnClickListener { spellclothClicked() }
    }

    private fun spellclothClicked() {
        // create a notification 4 days from now
        val notificationWork: OneTimeWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(92, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueue(notificationWork);
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