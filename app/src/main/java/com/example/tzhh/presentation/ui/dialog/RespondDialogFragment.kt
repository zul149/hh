package com.example.tzhh.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tzhh.databinding.FragmentRespondDialogBinding

class RespondDialogFragment : DialogFragment() {

    private var _binding: FragmentRespondDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRespondDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Логика кнопки "Добавить сопроводительное"
        binding.addCoverLetterButton.setOnClickListener {
            binding.addCoverLetterButton.visibility = View.GONE
            binding.coverLetterInput.visibility = View.VISIBLE
        }

        // Логика кнопки "Откликнуться"
        binding.finalRespondButton.setOnClickListener {
            // Закрываем диалог
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.let { window ->
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Прозрачный фон
            val params = window.attributes
            params.gravity = Gravity.BOTTOM // Привязка к нижнему краю
            window.attributes = params
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Освобождаем binding
    }
}