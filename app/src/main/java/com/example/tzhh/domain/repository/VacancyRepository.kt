package com.example.tzhh.domain.repository

import com.example.tzhh.domain.model.Vacancy

// Интерфейс, который создан для взаимодействия с данными.
interface VacancyRepository {
    suspend fun getVacancies(): List<Vacancy>
}