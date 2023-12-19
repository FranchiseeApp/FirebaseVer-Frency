package com.dicoding.frency.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun formatNumber(numberString: String): String {
    return try {
        val number = numberString.toInt()
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        formatter.format(number.toLong())
    } catch (e: NumberFormatException) {
        // Tangani kesalahan jika format String tidak sesuai dengan Integer
        "Invalid Number"
    }
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun String.showMessage(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}