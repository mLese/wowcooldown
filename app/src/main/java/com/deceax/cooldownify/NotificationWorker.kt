package com.deceax.cooldownify

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(val context: Context, params: WorkerParameters): Worker(context, params) {
    val CHANNEL_ID = "cooldownify"

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.inv_fabric_spellfire)
                .setContentTitle("Spellfire CD Ready")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notify(System.currentTimeMillis().toInt(), builder.build())

            val sharedPreferences = context.getSharedPreferences("cooldownify", MODE_PRIVATE)
            sharedPreferences.edit().putLong("spellcloth", -1)
        }
    }
}