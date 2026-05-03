package com.vk_edu.feed_and_eat.features.search.pres

import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.dishes.domain.models.CollectionRecipes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Tag
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import com.vk_edu.feed_and_eat.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val recipesRepo: RecipesRepository = mockk(relaxed = true)
    private val usersRepo: UsersRepository = mockk(relaxed = true)

    private lateinit var viewModel: SearchScreenViewModel

    @Before
    fun setUp() {
        // Успешные ответы по умолчанию — init{} не упадёт
        coEvery { usersRepo.getUserCollections() } returns flowOf(Response.Success(emptyList()))
        coEvery { recipesRepo.loadTags() } returns flowOf(Response.Success(emptyList()))

        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)
    }

    // -------------------------------------------------------------------------
    // requestChanged
    // -------------------------------------------------------------------------

    @Test
    fun `requestChanged — обновляет searchForm`() {
        viewModel.requestChanged("паста")

        assertEquals("паста", viewModel.searchForm.value)
    }

    @Test
    fun `requestChanged — несколько вызовов — последнее значение остаётся`() {
        viewModel.requestChanged("супы")
        viewModel.requestChanged("салаты")
        viewModel.requestChanged("десерты")

        assertEquals("десерты", viewModel.searchForm.value)
    }

    // -------------------------------------------------------------------------
    // setRequest — капитализация первой буквы
    // -------------------------------------------------------------------------

    @Test
    fun `setRequest — устанавливает reloadData в true`() = runTest {
        viewModel.requestChanged("борщ")
        viewModel.setRequest()

        assertTrue(viewModel.reloadData.value)
    }

    @Test
    fun `reloadDataFinished — сбрасывает reloadData в false`() = runTest {
        viewModel.setRequest()

        viewModel.reloadDataFinished()

        assertFalse(viewModel.reloadData.value)
    }

    // -------------------------------------------------------------------------
    // sortingChanged
    // -------------------------------------------------------------------------

    @Test
    fun `sortingChanged — обновляет sortingForm`() {
        viewModel.sortingChanged(2)

        assertEquals(2, viewModel.sortingForm.value)
    }

    @Test
    fun `sortingChanged — значение 0 сбрасывает сортировку`() {
        viewModel.sortingChanged(3)
        viewModel.sortingChanged(0)

        assertEquals(0, viewModel.sortingForm.value)
    }

    // -------------------------------------------------------------------------
    // tagCheckingChanged
    // -------------------------------------------------------------------------

    @Test
    fun `tagCheckingChanged — инвертирует checked у тега`() = runTest {
        val tags = listOf(
            Tag("Завтрак"),
            Tag("Обед"),
            Tag("Ужин"),
        )
        coEvery { recipesRepo.loadTags() } returns flowOf(Response.Success(tags))
        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)

        // Изначально все теги не выбраны
        assertFalse(viewModel.filtersForm.value.tags[1].ckecked)

        viewModel.tagCheckingChanged(1) // Выбрать "Обед"
        assertTrue(viewModel.filtersForm.value.tags[1].ckecked)

        viewModel.tagCheckingChanged(1) // Снять выбор "Обед"
        assertFalse(viewModel.filtersForm.value.tags[1].ckecked)
    }

    @Test
    fun `tagCheckingChanged — меняет только нужный тег, остальные не затрагивает`() = runTest {
        val tags = listOf(Tag("Завтрак"), Tag("Обед"), Tag("Ужин"))
        coEvery { recipesRepo.loadTags() } returns flowOf(Response.Success(tags))
        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)

        viewModel.tagCheckingChanged(0) // Выбрать "Завтрак"

        assertTrue(viewModel.filtersForm.value.tags[0].ckecked)
        assertFalse(viewModel.filtersForm.value.tags[1].ckecked)
        assertFalse(viewModel.filtersForm.value.tags[2].ckecked)
    }

    // -------------------------------------------------------------------------
    // nutrientMinChanged / nutrientMaxChanged
    // -------------------------------------------------------------------------

    @Test
    fun `nutrientMinChanged — обновляет min для указанного нутриента`() {
        viewModel.nutrientMinChanged(Nutrient.CALORIES.value, "100")

        assertEquals("100", viewModel.filtersForm.value.nutrients[Nutrient.CALORIES.value].min)
    }

    @Test
    fun `nutrientMaxChanged — обновляет max для указанного нутриента`() {
        viewModel.nutrientMaxChanged(Nutrient.CALORIES.value, "500")

        assertEquals("500", viewModel.filtersForm.value.nutrients[Nutrient.CALORIES.value].max)
    }

    @Test
    fun `nutrientMinChanged — не затрагивает max`() {
        viewModel.nutrientMaxChanged(Nutrient.PROTEIN.value, "80")
        viewModel.nutrientMinChanged(Nutrient.PROTEIN.value, "20")

        val nutrient = viewModel.filtersForm.value.nutrients[Nutrient.PROTEIN.value]
        assertEquals("20", nutrient.min)
        assertEquals("80", nutrient.max)
    }

    @Test
    fun `nutrientMaxChanged — не затрагивает min`() {
        viewModel.nutrientMinChanged(Nutrient.FAT.value, "5")
        viewModel.nutrientMaxChanged(Nutrient.FAT.value, "50")

        val nutrient = viewModel.filtersForm.value.nutrients[Nutrient.FAT.value]
        assertEquals("5", nutrient.min)
        assertEquals("50", nutrient.max)
    }

    @Test
    fun `изменение нутриента не затрагивает другие нутриенты`() {
        viewModel.nutrientMinChanged(Nutrient.SUGAR.value, "10")

        // Все остальные нутриенты остаются с дефолтными значениями
        assertEquals("", viewModel.filtersForm.value.nutrients[Nutrient.CALORIES.value].min)
        assertEquals("", viewModel.filtersForm.value.nutrients[Nutrient.PROTEIN.value].min)
        assertEquals("", viewModel.filtersForm.value.nutrients[Nutrient.FAT.value].min)
        assertEquals("", viewModel.filtersForm.value.nutrients[Nutrient.CARBOHYDRATES.value].min)
    }

    // -------------------------------------------------------------------------
    // setSortingAndFilters
    // -------------------------------------------------------------------------

    @Test
    fun `setSortingAndFilters — устанавливает reloadData в true`() = runTest {
        viewModel.sortingChanged(1)

        viewModel.setSortingAndFilters()

        assertTrue(viewModel.reloadData.value)
    }

    // -------------------------------------------------------------------------
    // loadUserFavourites
    // -------------------------------------------------------------------------

    @Test
    fun `init — загружает теги из репозитория в filtersForm`() = runTest {
        val tags = listOf(Tag("Завтрак"), Tag("Обед"))
        coEvery { recipesRepo.loadTags() } returns flowOf(Response.Success(tags))

        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)

        val formTags = viewModel.filtersForm.value.tags
        assertEquals(2, formTags.size)
        assertEquals("Завтрак", formTags[0].name)
        assertEquals("Обед", formTags[1].name)
        assertFalse(formTags[0].ckecked)
    }

    @Test
    fun `init — находит коллекцию Избранное и загружает favouriteRecipeIds`() = runTest {
        val collections = listOf(
            CollectionDataModel(id = "fav-99", name = "Избранное"),
        )
        coEvery { usersRepo.getUserCollections() } returns flowOf(
            Response.Success(collections)
        )
        coEvery { recipesRepo.loadCollectionRecipesId("fav-99") } returns flowOf(
            Response.Success(CollectionRecipes(recipeIds = listOf("id-1", "id-2")))
        )

        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)

        assertEquals("fav-99", viewModel.favouritesCollectionId.value)
        assertEquals(listOf("id-1", "id-2"), viewModel.favouriteRecipeIds.value)
    }

    @Test
    fun `init — нет коллекции Избранное — favouriteRecipeIds пуст`() = runTest {
        coEvery { usersRepo.getUserCollections() } returns flowOf(
            Response.Success(listOf(CollectionDataModel(id = "c1", name = "Другие рецепты")))
        )

        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)

        assertNull(viewModel.favouritesCollectionId.value)
        assertTrue(viewModel.favouriteRecipeIds.value.isEmpty())
    }

    // -------------------------------------------------------------------------
    // addRecipeToUserCollection
    // -------------------------------------------------------------------------

    @Test
    fun `addRecipeToUserCollection — успех — ID добавляется в favouriteRecipeIds`() = runTest {
        coEvery {
            recipesRepo.addRecipeToUserCollection("fav-1", "new-id", any())
        } returns flowOf(Response.Success(Unit))

        val card = RecipeCard(recipeId = "new-id", image = "https://img.com/1.jpg")
        viewModel.addRecipeToUserCollection("fav-1", card)

        assertTrue(viewModel.favouriteRecipeIds.value.contains("new-id"))
    }

    @Test
    fun `addRecipeToUserCollection — ошибка — errorMessage не null`() = runTest {
        coEvery {
            recipesRepo.addRecipeToUserCollection(any(), any(), any())
        } returns flowOf(Response.Failure(RuntimeException("Ошибка добавления")))

        viewModel.addRecipeToUserCollection("col-1", RecipeCard(recipeId = "x"))

        assertNotNull(viewModel.errorMessage.value)
    }

    // -------------------------------------------------------------------------
    // removeRecipeFromUserCollection
    // -------------------------------------------------------------------------

    @Test
    fun `removeRecipeFromUserCollection — успех — ID удаляется из favouriteRecipeIds`() = runTest {
        // Инициализируем с двумя избранными рецептами
        coEvery { usersRepo.getUserCollections() } returns flowOf(
            Response.Success(listOf(CollectionDataModel(id = "fav-1", name = "Избранное")))
        )
        coEvery { recipesRepo.loadCollectionRecipesId("fav-1") } returns flowOf(
            Response.Success(CollectionRecipes(recipeIds = listOf("keep-id", "remove-id")))
        )
        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)
        assertEquals(listOf("keep-id", "remove-id"), viewModel.favouriteRecipeIds.value)

        coEvery {
            recipesRepo.removeRecipeFromUserCollection("fav-1", "remove-id")
        } returns flowOf(Response.Success(Unit))

        val card = RecipeCard(recipeId = "remove-id")
        viewModel.removeRecipeFromUserCollection("fav-1", card)

        assertFalse(viewModel.favouriteRecipeIds.value.contains("remove-id"))
        assertTrue(viewModel.favouriteRecipeIds.value.contains("keep-id"))
    }

    // -------------------------------------------------------------------------
    // clearError
    // -------------------------------------------------------------------------

    @Test
    fun `clearError — сбрасывает errorMessage в null`() = runTest {
        coEvery { recipesRepo.loadTags() } returns flowOf(
            Response.Failure(RuntimeException("Ошибка тегов"))
        )
        viewModel = SearchScreenViewModel(recipesRepo, usersRepo)
        assertNotNull(viewModel.errorMessage.value)

        viewModel.clearError()

        assertNull(viewModel.errorMessage.value)
    }
}
