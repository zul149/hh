package com.example.tzhh.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.tzhh.R
import com.example.tzhh.databinding.ActivityVerifyCodeBinding
import com.example.tzhh.ui.verifycode.VerifyCodeViewModel

class VerifyCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyCodeBinding
    private val viewModel: VerifyCodeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verify_code)

        binding = ActivityVerifyCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Удаляем ActionBar
        supportActionBar?.hide()

        val email = intent.getStringExtra("EMAIL") ?: ""
        binding.tvVerificationMessage.text = "Отправили код на $email"

        setupTextWatchers()
        setupObservers()

        // Устанавливаем фокус на первую ячейку
        binding.etCode1.requestFocus()

        binding.btnConfirm.setOnClickListener {
            viewModel.onConfirmClicked()
        }

    }

    private fun setupTextWatchers() {
        val textWatchers = arrayOf(
            binding.etCode1,
            binding.etCode2,
            binding.etCode3,
            binding.etCode4
        )

        textWatchers.forEachIndexed { index, editText ->
            editText.addTextChangedListener { text ->
                if (text.isNullOrEmpty()) return@addTextChangedListener

                viewModel.onCodeChanged(text.toString(), index)
                // Автоматический переход к следующей ячейке
                if (index < textWatchers.size - 1) {
                    textWatchers[index + 1].requestFocus()
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.isButtonEnabled.observe(this) { isEnabled ->
            binding.btnConfirm.isEnabled = isEnabled
            binding.btnConfirm.setBackgroundColor(
                if (isEnabled) getColor(R.color.ButtonActive) else getColor(R.color.ButtonInactive)
            )
        }

        viewModel.navigateToMainActivity.observe(this) {
            // Переход на MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Закрыть текущую активность
        }
    }

}