package com.vk_edu.feed_and_eat.features.main.pres

import android.util.Log
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.dishes.domain.models.CollectionRecipes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Ingredient
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import com.vk_edu.feed_and_eat.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val recipesRepo: RecipesRepository = mockk(relaxed = true)
    private val usersRepo: UsersRepository = mockk(relaxed = true)

    /** Вспомогательный рецепт для тестов */
    private val sampleRecipe = Recipe(
        id = "recipe-1",
        name = "Борщ",
        image = "https://example.com/borscht.jpg",
        ingredients = listOf(
            Ingredient("Свёкла", 200.0, "г"),
            Ingredient("Капуста", 150.0, "г"),
        ),
        instructions = listOf(
            Instruction("Нарезать овощи"),
            Instruction("Варить 40 минут"),
        ),
        rating = 4.8,
        cooked = 120,
    )

    @Before
    fun setUpLog() {
        // android.util.Log.println_native — @FastNative метод, не покрывается
        // isReturnDefaultValues. Мокаем статически через MockK.
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.w(any(), any<Throwable>()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
    }

    /**
     * Создаёт ViewModel с настроенными по умолчанию моками (пустые/null ответы).
     */
    private fun createViewModel(): HomeScreenViewModel {
        coEvery { recipesRepo.loadDailyRecipe() } returns flowOf(Response.Success(null))
        coEvery { recipesRepo.loadTopRatingRecipes() } returns flowOf(Response.Success(emptyList()))
        coEvery { recipesRepo.loadLowCalorieRecipes() } returns flowOf(Response.Success(emptyList()))
        coEvery { recipesRepo.loadLastAddedRecipes() } returns flowOf(Response.Success(emptyList()))
        coEvery { recipesRepo.loadBreakfastRecipes() } returns flowOf(Response.Success(emptyList()))
        coEvery { usersRepo.getUserCollections() } returns flowOf(Response.Success(emptyList()))
        return HomeScreenViewModel(recipesRepo, usersRepo)
    }

    // -------------------------------------------------------------------------
    // getLargeCardData
    // -------------------------------------------------------------------------

    @Test
    fun `getLargeCardData — рецепт из репозитория корректно маппится в RecipeCard`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle() // дать init{} завершиться

        coEvery { recipesRepo.loadDailyRecipe() } returns flowOf(
            Response.Loading,
            Response.Success(sampleRecipe)
        )

        viewModel.getLargeCardData()
        advanceUntilIdle()

        val card = viewModel.largeCardData.value
        assertEquals("recipe-1", card.recipeId)
        assertEquals("Борщ", card.name)
        assertEquals("https://example.com/borscht.jpg", card.image)
        assertEquals(2, card.ingredients)
        assertEquals(2, card.steps)
        assertEquals(4.8, card.rating, 0.001)
        assertEquals(120, card.cooked)
    }

    @Test
    fun `getLargeCardData — null ответ от репозитория не изменяет largeCardData`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val defaultCard = RecipeCard()
        coEvery { recipesRepo.loadDailyRecipe() } returns flowOf(Response.Success(null))

        viewModel.getLargeCardData()
        advanceUntilIdle()

        assertEquals(defaultCard, viewModel.largeCardData.value)
    }

    @Test
    fun `getLargeCardData — Response Failure — errorMessage не null`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val error = RuntimeException("Нет связи с сервером")
        coEvery { recipesRepo.loadDailyRecipe() } returns flowOf(Response.Failure(error))

        viewModel.getLargeCardData()
        advanceUntilIdle()

        assertNotNull(viewModel.errorMessage.value)
    }

    // -------------------------------------------------------------------------
    // getCardsDataOfRow1 (Top Rating)
    // -------------------------------------------------------------------------

    @Test
    fun `getCardsDataOfRow1 — список рецептов маппится в список RecipeCard`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val recipes = listOf(sampleRecipe, sampleRecipe.copy(id = "recipe-2", name = "Пельмени"))
        coEvery { recipesRepo.loadTopRatingRecipes() } returns flowOf(Response.Success(recipes))

        viewModel.getCardsDataOfRow1()
        advanceUntilIdle()

        val cards = viewModel.cardsDataOfRow1.value
        assertEquals(2, cards.size)
        assertEquals("recipe-1", cards[0].recipeId)
        assertEquals("Пельмени", cards[1].name)
    }

    @Test
    fun `getCardsDataOfRow1 — пустой список — cardsDataOfRow1 пуст`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        coEvery { recipesRepo.loadTopRatingRecipes() } returns flowOf(Response.Success(emptyList()))

        viewModel.getCardsDataOfRow1()
        advanceUntilIdle()

        assertTrue(viewModel.cardsDataOfRow1.value.isEmpty())
    }

    // -------------------------------------------------------------------------
    // getFavouriteRecipeIds
    // -------------------------------------------------------------------------

    @Test
    fun `getFavouriteRecipeIds — находит коллекцию Избранное и загружает ID рецептов`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val collections = listOf(
            CollectionDataModel(id = "col-1", name = "Мои рецепты"),
            CollectionDataModel(id = "fav-42", name = "Избранное"),
        )
        coEvery { usersRepo.getUserCollections() } returns flowOf(Response.Success(collections))
        coEvery { recipesRepo.loadCollectionRecipesId("fav-42") } returns flowOf(
            Response.Success(CollectionRecipes(recipeIds = listOf("r1", "r2", "r3")))
        )

        viewModel.getFavouriteRecipeIds()
        advanceUntilIdle()

        assertEquals("fav-42", viewModel.favouritesCollectionId.value)
        assertEquals(listOf("r1", "r2", "r3"), viewModel.favouriteRecipeIds.value)
    }

    @Test
    fun `getFavouriteRecipeIds — нет коллекции Избранное — favouritesCollectionId остаётся null`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val collections = listOf(CollectionDataModel(id = "col-1", name = "Завтраки"))
        coEvery { usersRepo.getUserCollections() } returns flowOf(Response.Success(collections))

        viewModel.getFavouriteRecipeIds()
        advanceUntilIdle()

        assertNull(viewModel.favouritesCollectionId.value)
        assertTrue(viewModel.favouriteRecipeIds.value.isEmpty())
    }

    @Test
    fun `getFavouriteRecipeIds — пустые коллекции — favouriteRecipeIds остаётся пустым`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        coEvery { usersRepo.getUserCollections() } returns flowOf(Response.Success(emptyList()))

        viewModel.getFavouriteRecipeIds()
        advanceUntilIdle()

        assertTrue(viewModel.favouriteRecipeIds.value.isEmpty())
    }

    // -------------------------------------------------------------------------
    // addRecipeToUserCollection
    // -------------------------------------------------------------------------

    @Test
    fun `addRecipeToUserCollection — успех — ID рецепта появляется в favouriteRecipeIds`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Загружаем Избранное с одним рецептом
        coEvery { usersRepo.getUserCollections() } returns flowOf(
            Response.Success(listOf(CollectionDataModel(id = "fav-1", name = "Избранное")))
        )
        coEvery { recipesRepo.loadCollectionRecipesId("fav-1") } returns flowOf(
            Response.Success(CollectionRecipes(recipeIds = listOf("existing-id")))
        )
        viewModel.getFavouriteRecipeIds()
        advanceUntilIdle()

        coEvery {
            recipesRepo.addRecipeToUserCollection("fav-1", "new-recipe", any())
        } returns flowOf(Response.Success(Unit))

        val card = RecipeCard(recipeId = "new-recipe", image = "https://img.com/1.jpg")
        viewModel.addRecipeToUserCollection("fav-1", card)
        advanceUntilIdle()

        assertTrue(viewModel.favouriteRecipeIds.value.contains("new-recipe"))
    }

    @Test
    fun `addRecipeToUserCollection — ошибка — errorMessage не null`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val error = RuntimeException("Ошибка при добавлении")
        coEvery {
            recipesRepo.addRecipeToUserCollection(any(), any(), any())
        } returns flowOf(Response.Failure(error))

        val card = RecipeCard(recipeId = "recipe-x")
        viewModel.addRecipeToUserCollection("col-1", card)
        advanceUntilIdle()

        assertNotNull(viewModel.errorMessage.value)
    }

    // -------------------------------------------------------------------------
    // removeRecipeFromUserCollection
    // -------------------------------------------------------------------------

    @Test
    fun `removeRecipeFromUserCollection — успех — ID рецепта удаляется из favouriteRecipeIds`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Настраиваем начальное состояние: Избранное с двумя рецептами
        coEvery { usersRepo.getUserCollections() } returns flowOf(
            Response.Success(listOf(CollectionDataModel(id = "fav-1", name = "Избранное")))
        )
        coEvery { recipesRepo.loadCollectionRecipesId("fav-1") } returns flowOf(
            Response.Success(CollectionRecipes(recipeIds = listOf("r1", "r2")))
        )
        viewModel.getFavouriteRecipeIds()
        advanceUntilIdle()
        assertEquals(listOf("r1", "r2"), viewModel.favouriteRecipeIds.value)

        coEvery {
            recipesRepo.removeRecipeFromUserCollection("fav-1", "r1")
        } returns flowOf(Response.Success(Unit))

        val card = RecipeCard(recipeId = "r1")
        viewModel.removeRecipeFromUserCollection("fav-1", card)
        advanceUntilIdle()

        assertTrue(!viewModel.favouriteRecipeIds.value.contains("r1"))
        assertTrue(viewModel.favouriteRecipeIds.value.contains("r2"))
    }

    // -------------------------------------------------------------------------
    // clearError
    // -------------------------------------------------------------------------

    @Test
    fun `clearError — сбрасывает errorMessage в null`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        coEvery { recipesRepo.loadDailyRecipe() } returns flowOf(
            Response.Failure(RuntimeException("Ошибка"))
        )
        viewModel.getLargeCardData()
        advanceUntilIdle()
        assertNotNull(viewModel.errorMessage.value)

        viewModel.clearError()

        assertNull(viewModel.errorMessage.value)
    }
}
