package com.logger.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let {
            val packageName = it.packageName
            val notification = it.notification
            val extras = notification.extras

            // Filter notifications related to calls
            if (packageName.contains("call") || packageName.contains("dialer")) {
                // Retrieve details from the notification extras (if available)
                val callNumber = extras.getString("android.phone.extra.PHONE_NUMBER") ?: "Unknown"
                val callTime = System.currentTimeMillis()

                // Create JSON object for call log details
                val callLog = JSONObject()
                callLog.put("number", callNumber)
                callLog.put("time", callTime)
                callLog.put("type", "incoming/outgoing") // Adjust as needed for your use case

                // Write the call log as JSON into the data folder
                LogFileUtil.writeCallLog(callLog)
            }
            // Filter notifications related to SMS or MMS
            else if (packageName.contains("mms") || packageName.contains("sms")) {
                val smsContent = extras.getCharSequence("android.text")?.toString() ?: ""
                val sender = extras.getString("sender") ?: "Unknown"
                val currentTime = System.currentTimeMillis()

                // Prepare SMS log as text format
                val smsLog = """
                    time: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(currentTime))}
                    date: ${SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(Date(currentTime))}
                    sender: $sender

                    $smsContent
                """.trimIndent()

                // Write the SMS log to the INBOX folder (for outgoing SMS, a similar approach can be used)
                LogFileUtil.writeSmsLog(smsLog, inbox = true)
            }
        }
    }

    companion object {
        // Retrieve enabled notification listener package names
        fun getEnabledListenerPackages(context: android.content.Context): Set<String> {
            return android.provider.Settings.Secure.getString(
                context.contentResolver, "enabled_notification_listeners"
            )?.split(":")?.map {
                it.substringAfter("/")
            }?.toSet() ?: emptySet()
        }
    }
}
