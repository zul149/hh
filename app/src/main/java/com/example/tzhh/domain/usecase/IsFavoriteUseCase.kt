package com.example.tzhh.domain.usecase

import com.example.tzhh.domain.repository.FavoriteRepository

class IsFavoriteUseCase(private val repository: FavoriteRepository) {
    suspend fun invoke(vacancyId: String): Boolean {
        return repository.isFavorite(vacancyId)
    }
}