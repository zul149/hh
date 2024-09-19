package com.example.tzhh.presentation.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.tzhh.R


import com.example.tzhh.databinding.ActivityLoginBinding
import com.example.tzhh.ui.Login.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Удаляем ActionBar
        supportActionBar?.hide()

        // Устанавливаем обработчик изменений текста в EditText
        binding.etEmail.addTextChangedListener {
            viewModel.onEmailChanged(it.toString().trim())
        }


        // Обработка клика на кнопку очистки
        binding.etEmail.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etEmail.compoundDrawables[2] // Drawable справа
                if (drawableEnd != null && event.rawX >= (binding.etEmail.right - drawableEnd.bounds.width())) {
                    binding.etEmail.text.clear()
                    viewModel.onClearEmailClicked()
                    true
                }
            }
            false
        }

        // Наблюдение за состоянием кнопки очистки
        viewModel.showClearButton.observe(this) { show ->
            binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, if (show) R.drawable.clear else 0, 0)
        }

        // Обработка клика на кнопку Продолжить
        binding.btnContinue.setOnClickListener {
            viewModel.onContinueClicked()
        }

        // Наблюдение за состоянием кнопки
        viewModel.isButtonEnabled.observe(this) { isEnabled ->
            binding.btnContinue.isEnabled = isEnabled
            binding.btnContinue.setBackgroundColor(
                if (isEnabled) getColor(R.color.ButtonActive) else getColor(R.color.ButtonInactive)
            )
        }

        // Наблюдение за ошибкой email
        viewModel.emailError.observe(this) { hasError ->
            binding.error.visibility = if (hasError) View.VISIBLE else View.GONE
            binding.etEmail.background = if (hasError) {
                getDrawable(R.drawable.rounded_edit_text_error)
            } else {
                getDrawable(R.drawable.rounded_edit_text)
            }
        }


        viewModel.navigateToNextScreen.observe(this) {
            val email = binding.etEmail.text.toString()
            val intent = Intent(this, VerifyCodeActivity::class.java).apply {
                putExtra("EMAIL", email)
            }
            startActivity(intent)
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_search, R.id.navigation_favorite, R.id.navigation_feedback, R.id.navigation_messages, R.id.navigation_profile, -> {
                    // Пользователь остается на экране входа
                    true
                }
                else -> false
            }
        }
    }

}