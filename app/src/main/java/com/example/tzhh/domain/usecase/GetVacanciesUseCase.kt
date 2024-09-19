package com.example.tzhh.domain.usecase

import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.repository.VacancyRepository

class GetVacanciesUseCase(private val repository: VacancyRepository) {
    suspend fun execute(): List<Vacancy> {
        return repository.getVacancies() // Взаимодействие с репозиторием для получения вакансий
    }
}



