package com.example.tzhh.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tzhh.adapters.VacancyAdapter
import com.example.tzhh.data.database.AppDatabase
import com.example.tzhh.data.repository.FavoriteRepositoryImpl
import com.example.tzhh.databinding.FragmentFavoriteBinding
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.GetFavoritesUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase
import com.example.tzhh.presentation.ui.favorite.FavoriteViewModelFactory
import com.example.tzhh.presentation.ui.search.SearchFragmentDirections


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel
    // Объявляем адаптер как поле класса
    private lateinit var favoriteAdapter: VacancyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteDao = AppDatabase.getDatabase(requireContext()).favoriteDao()
        val favoriteRepository = FavoriteRepositoryImpl(favoriteDao)
        // Инициализация ViewModel с фабрикой
        val addFavoriteUseCase = AddFavoriteUseCase(favoriteRepository)
        val removeFavoriteUseCase = RemoveFavoriteUseCase(favoriteRepository)
        val getFavoritesUseCase = GetFavoritesUseCase(favoriteRepository)
        val isFavoriteUseCase = IsFavoriteUseCase(favoriteRepository)

        val viewModelFactory = FavoriteViewModelFactory(
            addFavoriteUseCase = addFavoriteUseCase,
            removeFavoriteUseCase = removeFavoriteUseCase,
            getFavoritesUseCase = getFavoritesUseCase,
            isFavoriteUseCase = isFavoriteUseCase
        )

        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]

        setupUI()
    }

    private fun setupUI() {
        // Настройка адаптера
        favoriteAdapter = VacancyAdapter(emptyList(), { vacancyId ->
            val vacancy = viewModel.favoriteVacancies.value?.find { it.id == vacancyId }
            val isFavorite = vacancy?.isFavorite ?: false
            val action = SearchFragmentDirections.actionSearchFragmentToVacancyDetailFragment(vacancyId, isFavorite)
            findNavController().navigate(action)
        }, { vacancyId ->
            // Логика удаления из избранного при клике на иконку
            viewModel.favoriteVacancies.value?.let { favorites ->
                val vacancy = favorites.find { it.id == vacancyId }
                vacancy?.let {
                    viewModel.toggleFavorite(it)
                    vacancy.isFavorite = false // Обновляем статус избранного
                    favoriteAdapter.notifyItemChanged(favorites.indexOf(it)) // Обновляем адаптер
                }
            }
        })

        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteRecyclerView.adapter = favoriteAdapter

        // Наблюдаем за изменением списка избранных вакансий
        viewModel.favoriteVacancies.observe(viewLifecycleOwner) { favorites ->
            favoriteAdapter.updateData(favorites)
            // Обновление текста TextView с учетом количества вакансий
            val count = favorites.size
            binding.favoriteCountTextView.text = getVacancyCountText(count)
        }
    }

    private fun getVacancyCountText(count: Int): String {
        val word = when {
            count % 100 in 11..19 -> "вакансий"
            count % 10 == 1 -> "вакансия"
            count % 10 in 2..4 -> "вакансии"
            else -> "вакансий"
        }
        return "$count $word"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}