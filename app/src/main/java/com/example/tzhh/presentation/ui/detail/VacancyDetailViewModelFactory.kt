package com.example.tzhh.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase
import com.example.tzhh.ui.detail.VacancyDetailViewModel

class VacancyDetailViewModelFactory(
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VacancyDetailViewModel::class.java)) {
            return VacancyDetailViewModel(
                isFavoriteUseCase,
                addFavoriteUseCase,
                removeFavoriteUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}