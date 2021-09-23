package com.example.clickstask

import java.text.SimpleDateFormat

class Utils {

    fun formatDate(date: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val output = SimpleDateFormat("EEEE,dd MMMM YYYY - hh:mm")
        return output.format(input.parse(date))
    }

}