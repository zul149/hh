package com.example.tzhh.data.repository

import com.example.tzhh.data.api.ApiService
import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.repository.VacancyRepository

class VacancyRepositoryImpl(
    private val apiService: ApiService
) : VacancyRepository {


    override suspend fun getVacancies(): List<Vacancy> {
        val response = apiService.getVacancies()
        if (response.isSuccessful) {
            return response.body()?.vacancies ?: emptyList()
        } else {
            // Обработка ошибки, выброс исключения или логирование
            return emptyList()
        }
    }


}