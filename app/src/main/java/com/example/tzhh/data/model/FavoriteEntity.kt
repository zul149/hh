package com.example.tzhh.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: AddressEntity,
    val company: String,
    val experience: ExperienceEntity,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: SalaryEntity,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
)

data class AddressEntity(
    val town: String,
    val street: String,
    val house: String
)

data class ExperienceEntity(
    val previewText: String,
    val text: String
)

data class SalaryEntity(
    val full: String
)