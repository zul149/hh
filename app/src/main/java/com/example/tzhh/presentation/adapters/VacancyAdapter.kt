package com.example.tzhh.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tzhh.R
import com.example.tzhh.databinding.ItemVacancyBinding
import com.example.tzhh.domain.model.Vacancy
import java.text.SimpleDateFormat
import java.util.Locale

class VacancyAdapter(
    private var vacancies: List<Vacancy>,
    private val onVacancyClick: (String) -> Unit, // Передаем только ID вакансии
    private val onFavoriteClick: (String) -> Unit
) : RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    override fun getItemCount(): Int = vacancies.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newVacancies: List<Vacancy>) {
        vacancies = newVacancies
        notifyDataSetChanged()
    }

    inner class VacancyViewHolder(private val binding: ItemVacancyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            binding.title.text = vacancy.title
            binding.address.text = vacancy.address.town
            binding.company.text = vacancy.company
            binding.experience.text = vacancy.experience.previewText
            binding.publishedDate.text = formatPublishedDate(vacancy.publishedDate)

            // Проверка состояния избранного
            if (vacancy.isFavorite) {
                binding.ivFavorite.setImageResource(R.drawable.favorite_filled) // Заполненная иконка
            } else {
                binding.ivFavorite.setImageResource(R.drawable.favorite) // Пустая иконка
            }


            binding.ivFavorite.setOnClickListener {
                if (vacancy.isFavorite) {
                    onFavoriteClick(vacancy.id) // Передаем ID для удаления из избранного
                    vacancy.isFavorite = false
                    notifyItemChanged(adapterPosition)
                } else {
                    onFavoriteClick(vacancy.id) // Передаем ID для добавления в избранное
                    vacancy.isFavorite = true
                    notifyItemChanged(adapterPosition)
                }
            }

            // Переход по клику на карточку вакансии
            binding.root.setOnClickListener {
                onVacancyClick(vacancy.id) // Передаем ID вакансии
            }

            // Проверка поля lookingNumber и его отображение
            val lookingNumber = vacancy.lookingNumber
            binding.lookingNumber.text = getLookingText(lookingNumber)
            binding.lookingNumber.visibility = View.VISIBLE

        }

    }
}

// Функция для правильного склонения слова "человек"
private fun getLookingText(lookingNumber: Int): String {
    val correctWord = when {
        lookingNumber % 100 in 11..19 -> "человек"
        lookingNumber % 10 == 1 -> "человек"
        lookingNumber % 10 in 2..4 -> "человека"
        else -> "человек"
    }

    return "Сейчас просматривает $lookingNumber $correctWord"
}

private fun formatPublishedDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMMM", Locale.getDefault())

    val date = inputFormat.parse(dateString) ?: return "Опубликовано неизвестно"

    // Форматируем дату в нужный формат
    val formattedDate = outputFormat.format(date)

    return "Опубликовано $formattedDate"
}