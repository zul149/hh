package com.example.tzhh.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.GetFavoritesUseCase
import com.example.tzhh.domain.usecase.GetVacanciesUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase

class SearchViewModelFactory(
    private val getVacanciesUseCase: GetVacanciesUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                getVacanciesUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}