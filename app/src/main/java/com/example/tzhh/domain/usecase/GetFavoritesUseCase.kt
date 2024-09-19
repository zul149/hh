package com.example.tzhh.domain.usecase

import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repository: FavoriteRepository) {
    fun invoke(): Flow<List<Vacancy>> {
        return repository.getFavorites()
    }
}