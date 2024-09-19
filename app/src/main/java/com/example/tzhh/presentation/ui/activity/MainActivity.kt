package com.example.tzhh.presentation.ui.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tzhh.R
import com.example.tzhh.databinding.ActivityMainBinding
import com.example.tzhh.ui.favorite.FavoriteViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Удаляем ActionBar
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_search,
                R.id.navigation_favorite,
                R.id.navigation_feedback,
                R.id.navigation_messages,
                R.id.navigation_profile,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // Инициализация ViewModel
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)


        // Наблюдаем за количеством избранных вакансий и обновляем бейдж
        favoriteViewModel.getFavoriteCount().observe(this) { count ->
            val menuItem = navView.getOrCreateBadge(R.id.navigation_favorite)

            if (count > 0) {
                menuItem.isVisible = true
                menuItem.number = count // Устанавливаем количество вакансий
            } else {
                menuItem.isVisible = false // Скрываем бейдж, если нет избранных
            }
        }
    }

//    // методы для активации/деактивации нижнего меню
//    fun setBottomNavigationEnabled(enabled: Boolean) {
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
//        bottomNavigationView.menu.setGroupEnabled(R.id.bottom_navigation_group, enabled)
//    }

}