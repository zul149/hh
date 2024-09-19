package com.example.tzhh.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tzhh.R
import com.example.tzhh.data.database.AppDatabase
import com.example.tzhh.data.repository.FavoriteRepositoryImpl
import com.example.tzhh.databinding.FragmentVacancyDetailBinding
import com.example.tzhh.domain.usecase.AddFavoriteUseCase
import com.example.tzhh.domain.usecase.IsFavoriteUseCase
import com.example.tzhh.domain.usecase.RemoveFavoriteUseCase
import com.example.tzhh.presentation.ui.detail.VacancyDetailViewModelFactory
import com.example.tzhh.ui.dialog.RespondDialogFragment

class VacancyDetailFragment : Fragment() {

    private var _binding: FragmentVacancyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VacancyDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // Деактивируем нижнее меню
//        (activity as? MainActivity)?.setBottomNavigationEnabled(false)

        // Получаем экземпляр репозитория
        val favoriteRepository = FavoriteRepositoryImpl(AppDatabase.getDatabase(requireContext()).favoriteDao())
        // Создаем экземпляры use cases
        val isFavoriteUseCase = IsFavoriteUseCase(favoriteRepository)
        val addFavoriteUseCase = AddFavoriteUseCase(favoriteRepository)
        val removeFavoriteUseCase = RemoveFavoriteUseCase(favoriteRepository)

        // Используйте фабрику для создания ViewModel
        val factory = VacancyDetailViewModelFactory(
            isFavoriteUseCase,
            addFavoriteUseCase,
            removeFavoriteUseCase
        )
        viewModel = ViewModelProvider(requireActivity(), factory)[VacancyDetailViewModel::class.java]

        // Получаем ID вакансии, переданное через Safe Args
        val vacancyId = arguments?.let {
            VacancyDetailFragmentArgs.fromBundle(it).vacancyId
        }

        // Передаем ID в ViewModel для загрузки деталей вакансии
        viewModel.loadVacancyDetails(vacancyId)


        // Наблюдаем за данными вакансии
        viewModel.vacancy.observe(viewLifecycleOwner) { vacancy ->
            // Обновляем UI с данными о вакансии
            binding.titleTextView.text = vacancy.title
            binding.companyName.text = vacancy.company
            binding.addressTextView.text = "${vacancy.address.town}, ${vacancy.address.street}, ${vacancy.address.house}"
            binding.experienceTextView.text = vacancy.experience.previewText
            binding.salaryTextView.text = vacancy.salary.full
            binding.scheduleTextView.text = vacancy.schedules.joinToString(", ")
            binding.applyCountTextView.text = "${vacancy.appliedNumber} человек уже откликнулись"
            binding.lookingCountTextView.text = "${vacancy.lookingNumber} человека сейчас смотрят"
            binding.companyDescriptionTextView.text = vacancy.description
            binding.tasksTextView.text = vacancy.responsibilities
            binding.questionsSection.removeAllViews() // Сначала очистите секцию вопросов

            vacancy.questions.forEach { question ->
                val button = Button(requireContext()).apply {
                    text = question
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_container_background)
                    setTextColor(Color.WHITE)
                    textSize = 14f
                    isAllCaps = false

                    // Устанавливаем внутренние отступы для текста (в пикселях)
                    val paddingInDp = 8
                    val scale = resources.displayMetrics.density
                    val paddingInPx = (paddingInDp * scale + 0.5f).toInt()
                    setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)
                }

                // Устанавливаем отступы с помощью LayoutParams
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 8, 8, 8)
                }

                // Применяем параметры к кнопке
                button.layoutParams = layoutParams

                binding.questionsSection.addView(button)

            }

            // Обработчик клика для иконки избранного
            binding.favoriteIcon.setOnClickListener {
                viewModel.toggleFavorite(vacancy)
            }

            // Наблюдаем за состоянием избранного
            viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
                if (isFavorite) {
                    binding.favoriteIcon.setImageResource(R.drawable.favorite_filled)
                } else {
                    binding.favoriteIcon.setImageResource(R.drawable.favorite)
                }
            }
        }

        // Устанавливаем слушатель на кнопку "Откликнуться"
        binding.respondButton.setOnClickListener {
            val respondDialog = RespondDialogFragment()
            respondDialog.show(parentFragmentManager, "RespondDialogFragment")
        }

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        // Активируем нижнее меню при уходе с фрагмента
//        (activity as? MainActivity)?.setBottomNavigationEnabled(true)
    }
}