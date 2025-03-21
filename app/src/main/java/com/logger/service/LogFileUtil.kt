package com.logger.service

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object LogFileUtil {

    // Returns the directory for storing log files within the app's internal storage.
    fun getAppDataDir(context: Context): File {
        val dir = File(context.filesDir, "logs")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    // Writes call log JSON object to a daily JSON file (DD_MM_YYYY.json)
    fun writeCallLog(callLog: JSONObject) {
        val context = MyApplication.getContext()
        val dir = getAppDataDir(context)
        val fileName = SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(Date()) + ".json"
        val file = File(dir, fileName)

        // Read existing JSON array or create a new one if file does not exist
        val logs = if (file.exists()) {
            val content = file.readText()
            try {
                JSONArray(content)
            } catch (e: Exception) {
                JSONArray()
            }
        } else {
            JSONArray()
        }

        logs.put(callLog)
        FileWriter(file, false).use { writer ->
            writer.write(logs.toString(4))
        }
    }

    // Writes SMS log as a text file in the appropriate folder (SMS/INBOX or SMS/OUTBOX)
    fun writeSmsLog(smsLog: String, inbox: Boolean) {
        val context = MyApplication.getContext()
        val dirName = if (inbox) "SMS/INBOX" else "SMS/OUTBOX"
        val dir = File(getAppDataDir(context), dirName)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val fileName = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(Date()) + ".txt"
        val file = File(dir, fileName)
        FileWriter(file, false).use { writer ->
            writer.write(smsLog)
        }
    }
}
