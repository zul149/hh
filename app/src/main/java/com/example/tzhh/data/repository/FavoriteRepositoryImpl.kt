package com.example.tzhh.data.repository

import com.example.tzhh.data.dao.FavoriteDao
import com.example.tzhh.data.model.AddressEntity
import com.example.tzhh.data.model.ExperienceEntity
import com.example.tzhh.data.model.FavoriteEntity
import com.example.tzhh.data.model.SalaryEntity
import com.example.tzhh.domain.model.Address
import com.example.tzhh.domain.model.Experience
import com.example.tzhh.domain.model.Salary
import com.example.tzhh.domain.model.Vacancy
import com.example.tzhh.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(private val favoriteDao: FavoriteDao) : FavoriteRepository {

    override suspend fun addFavorite(vacancy: Vacancy) {
        favoriteDao.insertFavorite(vacancy.toFavoriteEntity())
    }

    override suspend fun removeFavorite(vacancy: Vacancy) {
        favoriteDao.deleteFavorite(vacancy.toFavoriteEntity())
    }

    override fun getFavorites(): Flow<List<Vacancy>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toVacancy() }
        }
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return favoriteDao.getFavoriteById(vacancyId) != null
    }

    private fun Vacancy.toFavoriteEntity(): FavoriteEntity {
        return FavoriteEntity(
            id = this.id,
            lookingNumber = this.lookingNumber,
            title = this.title,
            address = AddressEntity(
                town = this.address.town,
                street = this.address.street,
                house = this.address.house
            ),
            company = this.company,
            experience = ExperienceEntity(
                previewText = this.experience.previewText,
                text = this.experience.text
            ),
            publishedDate = this.publishedDate,
            isFavorite = this.isFavorite,
            salary = SalaryEntity(
                full = this.salary.full
            ),
            schedules = this.schedules,
            appliedNumber = this.appliedNumber,
            description = this.description ?: "Описание отсутствует",
            responsibilities = this.responsibilities ?: "Обязанности не указаны",
            questions = this.questions ?: emptyList()
        )
    }

    private fun FavoriteEntity.toVacancy(): Vacancy {
        return Vacancy(
            id = this.id,
            lookingNumber = this.lookingNumber,
            title = this.title,
            address = Address(
                town = this.address.town,
                street = this.address.street,
                house = this.address.house
            ),
            company = this.company,
            experience = Experience(
                previewText = this.experience.previewText,
                text = this.experience.text
            ),
            publishedDate = this.publishedDate,
            isFavorite = this.isFavorite,
            salary = Salary(
                full = this.salary.full
            ),
            schedules = this.schedules,
            appliedNumber = this.appliedNumber,
            description = this.description ?: "Описание отсутствует",
            responsibilities = this.responsibilities ?: "Обязанности не указаны",
            questions = this.questions ?: emptyList()
        )
    }
}