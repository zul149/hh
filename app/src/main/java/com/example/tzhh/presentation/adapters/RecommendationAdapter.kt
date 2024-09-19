package com.example.tzhh.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tzhh.R
import com.example.tzhh.databinding.ItemRecommendationBinding
import com.example.tzhh.domain.model.Recommendation

class RecommendationAdapter(
    private val recommendations: List<Recommendation>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]
        holder.bind(recommendation, onItemClicked)
    }

    override fun getItemCount(): Int = recommendations.size

    class RecommendationViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: Recommendation, onItemClicked: (String) -> Unit) {
            // Обрабатываем хардкод иконок по id
            when (recommendation.id) {
                "near_vacancies" -> {
                    binding.icon.setImageResource(R.drawable.near_vacancies)
                    binding.icon.visibility = View.VISIBLE
                }
                "level_up_resume" -> {
                    binding.icon.setImageResource(R.drawable.level_up_resume)
                    binding.icon.visibility = View.VISIBLE
                }
                "temporary_job" -> {
                    binding.icon.setImageResource(R.drawable.temporary_job)
                    binding.icon.visibility = View.VISIBLE
                }
                else -> {
                    binding.icon.visibility = View.GONE
                }
            }

            // Устанавливаем заголовок
            binding.title.text = recommendation.title
            binding.title.maxLines = if (recommendation.button != null) 2 else 3
            binding.title.ellipsize = TextUtils.TruncateAt.END

            // Обрабатываем текст кнопки
            recommendation.button?.let {
                binding.buttonText.text = it.text
                binding.buttonText.visibility = View.VISIBLE
            } ?: run {
                binding.buttonText.visibility = View.GONE
            }

            // Клик по всей карточке с переходом по ссылке
            binding.root.setOnClickListener {
                onItemClicked(recommendation.link)
            }
        }
    }
}