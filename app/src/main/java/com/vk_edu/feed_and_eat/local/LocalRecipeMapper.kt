package com.vk_edu.feed_and_eat.local

import android.net.Uri
import com.vk_edu.feed_and_eat.BuildConfig
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Ingredient
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Nutrients
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Servings
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.local.db.entity.CachedRecipeEntity
import com.vk_edu.feed_and_eat.network.dto.RecipeDto
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object LocalRecipeMapper {
    private val json = Json { ignoreUnknownKeys = true }

    private fun makeFullUrl(relativeUrl: String?): String? {
        if (relativeUrl == null) return null
        val base = BuildConfig.API_BASE_URL.trimEnd('/')
        val url = if (!relativeUrl.startsWith("http")) {
            // Относительный путь — на нашем сервере
            "$base$relativeUrl"
        } else if (relativeUrl.startsWith(base)) {
            // Уже абсолютный URL нашего сервера — не трогаем
            relativeUrl
        } else {
            // Внешний URL (например img.spoonacular.com) — проксируем через наш сервер,
            "$base/image-proxy?url=${Uri.encode(relativeUrl)}"
        }
        return url
    }

    fun toEntity(dto: RecipeDto, now: Long = System.currentTimeMillis()): CachedRecipeEntity =
        CachedRecipeEntity(
            id = dto.id,
            name = dto.name,
            imageUrl = dto.imageUrl,
            rating = dto.rating,
            cooked = dto.cooked,
            payloadJson = json.encodeToString(RecipeDto.serializer(), dto),
            updatedAt = now,
        )

    fun entityToDomain(entity: CachedRecipeEntity): Recipe? {
        val dto = try {
            json.decodeFromString(RecipeDto.serializer(), entity.payloadJson)
        } catch (_: Exception) {
            return null
        }
        return dtoToDomain(dto)
    }

    fun dtoToDomain(dto: RecipeDto): Recipe {
        val createdDate = try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).parse(dto.createdAt)
        } catch (_: Exception) {
            try {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dto.createdAt)
            } catch (_: Exception) {
                Date()
            }
        }

        return Recipe(
            id = dto.id,
            name = dto.name,
            image = makeFullUrl(dto.imageUrl),
            instructions = dto.instructions.map { instructionDto ->
                Instruction(
                    paragraph = instructionDto.paragraph,
                    timers = instructionDto.timers?.map { timerDto ->
                        if (timerDto.type == "constant") {
                            Timer(
                                type = timerDto.type,
                                number = timerDto.number ?: 5,
                                lowerLimit = null,
                                upperLimit = null,
                                id = timerDto.id ?: UUID.randomUUID().toString(),
                            )
                        } else {
                            Timer(
                                type = timerDto.type,
                                number = null,
                                lowerLimit = timerDto.lowerLimit ?: 1,
                                upperLimit = timerDto.upperLimit ?: 5,
                                id = timerDto.id ?: UUID.randomUUID().toString(),
                            )
                        }
                    },
                )
            },
            servings = dto.servings?.let { Servings(amount = it.amount, weight = it.weight) },
            ingredients = dto.ingredients?.map { Ingredient(name = it.name, amount = it.amount, unit = it.unit) }
                ?: emptyList(),
            tags = dto.tags,
            nutrients = dto.nutrients?.let {
                Nutrients(
                    calories = it.calories,
                    sugar = it.sugar,
                    protein = it.protein,
                    fat = it.fat,
                    carbohydrates = it.carbohydrates,
                )
            } ?: Nutrients(),
            author = 0,
            user = dto.userId,
            rating = dto.rating,
            cooked = dto.cooked,
            reviews = null,
            created = createdDate,
        )
    }
}

