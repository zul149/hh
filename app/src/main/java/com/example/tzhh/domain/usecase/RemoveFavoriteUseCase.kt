package com.example.tzhh.domain.usecase

import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.repository.FavoriteRepository

class RemoveFavoriteUseCase(private val repository: FavoriteRepository) {
    suspend fun invoke(vacancy: Vacancy) {
        repository.removeFavorite(vacancy)
    }
}