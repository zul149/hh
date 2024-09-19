package com.example.tzhh.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tzhh.data.api.RetrofitClient
import com.example.tzhh.domain.model.Recommendation
import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.GetFavoritesUseCase
import com.example.tzhh.domain.usecase.GetVacanciesUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase

import kotlinx.coroutines.launch



class SearchViewModel(
    private val getVacanciesUseCase: GetVacanciesUseCase,
) : ViewModel() {

    private val _vacancies = MutableLiveData<List<Vacancy>>()
    val vacancies: LiveData<List<Vacancy>> get() = _vacancies

    private val _recommendations = MutableLiveData<List<Recommendation>>()
    val recommendations: LiveData<List<Recommendation>> get() = _recommendations

    private val _showAllVacancies = MutableLiveData<Boolean>()
    val showAllVacancies: LiveData<Boolean> get() = _showAllVacancies


    private val _showMoreButton = MutableLiveData<Boolean>()
    val showMoreButton: LiveData<Boolean> get() = _showMoreButton

    private val _showBackButton = MutableLiveData<Boolean>()
    val showBackButton: LiveData<Boolean> get() = _showBackButton

    private val _showVacancyCount = MutableLiveData<Boolean>()
    val showVacancyCount: LiveData<Boolean> get() = _showVacancyCount

    private val _showAdditionalFilter = MutableLiveData<Boolean>()
    val showAdditionalFilter: LiveData<Boolean> get() = _showAdditionalFilter

    init {
        _showAllVacancies.value = false // Изначально показываем только часть вакансий
        _showMoreButton.value = false
        _showBackButton.value = false
        _showVacancyCount.value = false
        _showAdditionalFilter.value = false
        loadVacancies() // Загрузка вакансий при инициализации
    }

    fun fetchRecommendations() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRecommendations()
                if (response.isSuccessful) {
                    _recommendations.value = response.body()?.offers ?: emptyList()
                } else {
                    // Обработка ошибки
                }
            } catch (e: Exception) {
                // Обработка исключений
            }
        }
    }

    fun loadVacancies() {
        viewModelScope.launch {
            val loadedVacancies = getVacanciesUseCase.execute()
            _vacancies.value = loadedVacancies

            // Проверка на необходимость кнопки "Показать ещё"
            _showMoreButton.value = loadedVacancies.size > 3
        }
    }

    fun toggleShowAllVacancies() {
        _showAllVacancies.value = _showAllVacancies.value?.not()
        _showBackButton.value = _showAllVacancies.value == true
        _showVacancyCount.value = _showAllVacancies.value == true
        _showAdditionalFilter.value = _showAllVacancies.value == true
        // Если показываем все вакансии, кнопка "Показать еще" скрывается
        _showMoreButton.value = _showAllVacancies.value == false
    }


}