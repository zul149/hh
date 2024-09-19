package com.example.tzhh.domain.repository

import com.example.tzhh.domain.model.Vacancy
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addFavorite(vacancy: Vacancy)
    suspend fun removeFavorite(vacancy: Vacancy)
    fun getFavorites(): Flow<List<Vacancy>>
    suspend fun isFavorite(vacancyId: String): Boolean
}