package com.example.tzhh.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.GetFavoritesUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase
import com.example.tzhh.ui.favorite.FavoriteViewModel

class FavoriteViewModelFactory(
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(
                addFavoriteUseCase = addFavoriteUseCase,
                removeFavoriteUseCase = removeFavoriteUseCase,
                getFavoritesUseCase = getFavoritesUseCase,
                isFavoriteUseCase = isFavoriteUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}