package com.example.tzhh.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.GetFavoritesUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.launch



class FavoriteViewModel(
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    // автоматического обновления пользовательского интерфейса при изменении данных избранных вакансий
    val favoriteVacancies: LiveData<List<Vacancy>> = getFavoritesUseCase.invoke().asLiveData()

    fun toggleFavorite(vacancy: Vacancy) {
        viewModelScope.launch {
            if (isFavoriteUseCase.invoke(vacancy.id)) {
                removeFavoriteUseCase.invoke(vacancy)
            } else {
                addFavoriteUseCase.invoke(vacancy)
            }

        }
    }

    // Функция для подсчета избранных вакансий
    fun getFavoriteCount(): LiveData<Int> {
        return favoriteVacancies.map { it.size }
    }


}