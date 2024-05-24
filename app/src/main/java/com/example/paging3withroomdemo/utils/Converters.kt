package com.example.paging3withroomdemo.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    @TypeConverter
    fun fromListToString(list: List<String?>?): String {
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toData(dataString: String?): List<String?>? {
        if (dataString.isNullOrEmpty()) {
            return mutableListOf()
        }
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(dataString, type)
    }
}