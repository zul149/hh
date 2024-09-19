package com.example.tzhh.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tzhh.data.api.RetrofitClient
import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.launch

class VacancyDetailViewModel(
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _vacancy = MutableLiveData<Vacancy>()
    val vacancy: LiveData<Vacancy> get() = _vacancy

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun loadVacancyDetails(vacancyId: String?) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getVacancies()
                if (response.isSuccessful) {
                    val vacancies = response.body()?.vacancies
                    _vacancy.value = vacancies?.find { it.id == vacancyId }
                } else {
                    // Обработка ошибки
                }
            } catch (e: Exception) {
                // Обработка исключений
            }
        }
        vacancyId?.let {
            viewModelScope.launch {
                _isFavorite.value = isFavoriteUseCase.invoke(it)
            }
        }
    }

    fun toggleFavorite(vacancy: Vacancy) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                removeFavoriteUseCase.invoke(vacancy)
                _isFavorite.value = false
            } else {
                addFavoriteUseCase.invoke(vacancy)
                _isFavorite.value = true
            }
        }
    }

}