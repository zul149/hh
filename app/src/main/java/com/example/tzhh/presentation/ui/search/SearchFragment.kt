package com.example.tzhh.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tzhh.adapters.RecommendationAdapter
import com.example.tzhh.adapters.VacancyAdapter
import com.example.tzhh.data.api.RetrofitClient
import com.example.tzhh.data.database.AppDatabase
import com.example.tzhh.data.repository.FavoriteRepositoryImpl
import com.example.tzhh.data.repository.VacancyRepositoryImpl
import com.example.tzhh.databinding.FragmentSearchBinding
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.GetFavoritesUseCase
import com.example.tzhh.domain.usecase.GetVacanciesUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase
import com.example.tzhh.presentation.ui.favorite.FavoriteViewModelFactory
import com.example.tzhh.ui.favorite.FavoriteViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // Объявляем фабрику для ViewModel
    private lateinit var viewModel: SearchViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    // Объявляем адаптер как поле класса
    private lateinit var vacancyAdapter: VacancyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Создаём экземпляр репозитория и UseCase
        val repository = VacancyRepositoryImpl(RetrofitClient.apiService)
        val getVacanciesUseCase = GetVacanciesUseCase(repository)
        // Создаем фабрику для ViewModel
        val viewModelFactory = SearchViewModelFactory(getVacanciesUseCase)
        // Инициализируем ViewModel через фабрику
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]


        // Инициализация FavoriteRepository и UseCase
        val favoriteRepository = FavoriteRepositoryImpl(AppDatabase.getDatabase(requireContext()).favoriteDao())
        val addFavoriteUseCase = AddFavoriteUseCase(favoriteRepository)
        val removeFavoriteUseCase = RemoveFavoriteUseCase(favoriteRepository)
        val getFavoritesUseCase = GetFavoritesUseCase(favoriteRepository)
        val isFavoriteUseCase = IsFavoriteUseCase(favoriteRepository)

        // Создаем фабрику для FavoriteViewModel
        val favoriteViewModelFactory = FavoriteViewModelFactory(
            addFavoriteUseCase = addFavoriteUseCase,
            removeFavoriteUseCase = removeFavoriteUseCase,
            getFavoritesUseCase = getFavoritesUseCase,
            isFavoriteUseCase = isFavoriteUseCase
        )
        // (requireActivity(),...) оба фрагмента и SearchFragment, и FavoriteFragment могут делить одну и ту же ViewModel.
        favoriteViewModel = ViewModelProvider(requireActivity(), favoriteViewModelFactory)[FavoriteViewModel::class.java]

        setupUI()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setupUI() {
        // Логика UI
        binding.recyclerViewRecommendations.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        // Наблюдение за данными из ViewModel
        viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            val recommendationAdapter = RecommendationAdapter(recommendations) { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
            binding.recyclerViewRecommendations.adapter = recommendationAdapter
        }
        // Загружаем рекомендации
        viewModel.fetchRecommendations()


        // Настройка адаптера для списка вакансий
        vacancyAdapter = VacancyAdapter(emptyList(), { vacancyId ->
            val vacancy = viewModel.vacancies.value?.find { it.id == vacancyId }
            val isFavorite = vacancy?.isFavorite ?: false
            // Переход к VacancyDetailFragment с передачей ID вакансии (аргументов)
            val action = SearchFragmentDirections.actionSearchFragmentToVacancyDetailFragment(vacancyId, isFavorite)
            findNavController().navigate(action)
        },{  vacancyId -> // передача списка избранных вакансий
            viewModel.vacancies.value?.let { vacancies ->
                val vacancy = vacancies.find { it.id == vacancyId }
                vacancy?.let {
                    // Просто вызываем ViewModel для переключения статуса избранного
                    favoriteViewModel.toggleFavorite(it)
                    vacancyAdapter.notifyItemChanged(vacancies.indexOf(it))
                }
            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = vacancyAdapter

        // Наблюдаем за изменением списка вакансий
        viewModel.vacancies.observe(viewLifecycleOwner) { vacancies ->
            val vacanciesToShow = if (viewModel.showAllVacancies.value == true) vacancies else vacancies.take(3)
            vacancyAdapter.updateData(vacanciesToShow)

            // Обновляем текст кнопки
            val remainingVacancies = vacancies.size - vacanciesToShow.size
            if (remainingVacancies > 0) {
                binding.btnMoreVacancies.text = "Ещё $remainingVacancies ${getVacancyText(remainingVacancies)}"
            }
        }

        // Наблюдаем за изменениями в списке избранного
        favoriteViewModel.favoriteVacancies.observe(viewLifecycleOwner) { favorites ->
            viewModel.vacancies.value?.let { vacancies ->
                vacancies.forEach { vacancy ->
                    vacancy.isFavorite = favorites.any { it.id == vacancy.id }
                }
                vacancyAdapter.notifyDataSetChanged()
            }
        }

        // Наблюдаем за состоянием кнопки "Показать еще"
        viewModel.showMoreButton.observe(viewLifecycleOwner) { showButton ->
            binding.btnMoreVacancies.visibility = if (showButton) View.VISIBLE else View.GONE
        }

        // Наблюдаем за изменениями видимости кнопки "Назад"
        viewModel.showBackButton.observe(viewLifecycleOwner) { show ->
            binding.backButton.visibility = if (show) View.VISIBLE else View.GONE
        }

        // Обрабатываем нажатие на кнопку
        binding.btnMoreVacancies.setOnClickListener {
            viewModel.toggleShowAllVacancies()
        }

        // Наблюдаем за состоянием отображения количества вакансий
        viewModel.showVacancyCount.observe(viewLifecycleOwner) { show ->
            binding.vacancyCount.visibility = if (show) View.VISIBLE else View.GONE
        }

        // Наблюдаем за состоянием отображения дополнительного фильтра
        viewModel.showAdditionalFilter.observe(viewLifecycleOwner) { show ->
            binding.additionalFilter.visibility = if (show) View.VISIBLE else View.GONE
        }

        // Логика кнопки "Назад"
        binding.backButton.setOnClickListener {
            viewModel.toggleShowAllVacancies()
        }

        // Обновляем количество вакансий при изменении состояния
        viewModel.showAllVacancies.observe(viewLifecycleOwner) { showAll ->
            viewModel.vacancies.value?.let { vacancies ->
                val vacanciesToShow = if (showAll) vacancies else vacancies.take(3)
                vacancyAdapter.updateData(vacanciesToShow)
                // Обновляем текст с количеством вакансий
                binding.vacancyCount.text = "${vacancies.size} ${getVacancyText(vacancies.size)}"
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Метод для правильного склонения слова "вакансия"
    private fun getVacancyText(number: Int): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "вакансия"
            number % 10 in 2..4 && number % 100 !in 12..14 -> "вакансии"
            else -> "вакансий"
        }
    }
}