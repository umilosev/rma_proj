package com.example.rma_proj_esus.cats.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun longToFormattedDate(timestamp: Long): String {
    val date = Date(timestamp)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(date)
}