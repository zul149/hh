package com.example.tzhh.data.api

import com.example.tzhh.data.model.RecommendationsResponse
import com.example.tzhh.data.model.VacanciesResponse
import com.example.tzhh.domain.model.Vacancy
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download")
    suspend fun getVacancies(): Response<VacanciesResponse>

    @GET("u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download")
    suspend fun getRecommendations(): Response<RecommendationsResponse>

    @GET("u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download")
    suspend fun getFavoriteVacancies(): List<Vacancy>
}