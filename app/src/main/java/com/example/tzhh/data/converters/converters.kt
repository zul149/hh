package com.example.tzhh.data.converters

import androidx.room.TypeConverter
import com.example.tzhh.data.model.AddressEntity
import com.example.tzhh.data.model.ExperienceEntity
import com.example.tzhh.data.model.SalaryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // Конвертер для AddressEntity
    @TypeConverter
    fun fromAddress(address: AddressEntity): String {
        return Gson().toJson(address)
    }

    @TypeConverter
    fun toAddress(json: String): AddressEntity {
        return Gson().fromJson(json, AddressEntity::class.java)
    }

    // Конвертер для ExperienceEntity
    @TypeConverter
    fun fromExperience(experience: ExperienceEntity): String {
        return Gson().toJson(experience)
    }

    @TypeConverter
    fun toExperience(json: String): ExperienceEntity {
        return Gson().fromJson(json, ExperienceEntity::class.java)
    }

    // Конвертер для SalaryEntity
    @TypeConverter
    fun fromSalary(salary: SalaryEntity): String {
        return Gson().toJson(salary)
    }

    @TypeConverter
    fun toSalary(json: String): SalaryEntity {
        return Gson().fromJson(json, SalaryEntity::class.java)
    }

    // Конвертер для списка schedules
    @TypeConverter
    fun fromSchedules(schedules: List<String>): String {
        return Gson().toJson(schedules)
    }

    @TypeConverter
    fun toSchedules(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }
}