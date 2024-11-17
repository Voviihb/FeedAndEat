package com.vk_edu.feed_and_eat.login_test

import com.vk_edu.feed_and_eat.MainDispatcherRule
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.pres.RegisterScreenViewModel
import com.vk_edu.feed_and_eat.features.login.pres.writeUserId
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoImpl
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class RegisterScreenViewModelTest {
    private lateinit var viewModel: RegisterScreenViewModel
    private lateinit var authRepo: AuthRepoImpl
    private lateinit var usersRepo: UsersRepoImpl
    private lateinit var recipesRepo: RecipesRepoImpl
    private lateinit var prefs: PreferencesManager

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        authRepo = mockk()
        usersRepo = mockk()
        recipesRepo = mockk()
        prefs = mockk()
        viewModel = RegisterScreenViewModel(
            _authRepo = authRepo,
            _usersRepo = usersRepo,
            _recipesRepo = recipesRepo,
            _preferencesManager = prefs
        )
    }

    @Test
    fun `test registerForm updates after changes`() = runTest {
        // Arrange
        val expectedEmail = "test@example.com"
        val expectedLogin = "test"
        val expectedPassword = "password"
        val expectedPasswordControl = "password"

        // Act
        viewModel.emailChanged(expectedEmail)
        viewModel.loginChanged(expectedLogin)
        viewModel.password1Changed(expectedPassword)
        viewModel.password2Changed(expectedPasswordControl)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        assert(viewModel.registerFormState.value.email == expectedEmail)
        { "Expected $expectedEmail, but was ${viewModel.registerFormState.value.email}" }
        assert(viewModel.registerFormState.value.login == expectedLogin)
        { "Expected $expectedLogin, but was ${viewModel.registerFormState.value.login}" }
        assert(viewModel.registerFormState.value.password == expectedPassword)
        { "Expected $expectedPassword, but was ${viewModel.registerFormState.value.password}" }
        assert(viewModel.registerFormState.value.passwordControl == expectedPasswordControl)
        { "Expected $expectedPasswordControl, but was ${viewModel.registerFormState.value.passwordControl}" }
    }

    @Test
    fun `registerUserWithEmail success navigates to HomeScreen`() = runTest {
        // Arrange
        val expectedEmail = "test@example.com"
        val expectedLogin = "test"
        val expectedPassword = "password"
        val expectedPasswordControl = "password"

        val mockUserId = "test_user_id"
        val mockCreateCollectionResult = "new_collection_id"
        val navigateMock: (String) -> Unit = mockk(relaxed = true)
        val mockUserData = UserModel(
            userId = mockUserId, collectionsIdList = listOf(
                CollectionDataModel(
                    id = mockCreateCollectionResult,
                    name = "Favourites"
                )
            )
        )

        coEvery { authRepo.firebaseSignUp(any(), any(), any()) } returns flow {
            emit(Response.Loading)
            emit(Response.Success(null))
        }
        coEvery { authRepo.getUserId() } returns mockUserId
        every { prefs.saveData(any(), any()) } just Runs
        coEvery { usersRepo.saveUserData(any(), any()) } returns flow {
            emit(Response.Loading)
            emit(Response.Success(Void::class.java.newInstance()))
        }
        coEvery { recipesRepo.createNewCollection() } returns flow {
            emit(Response.Loading)
            emit(Response.Success(mockCreateCollectionResult))
        }
        every { writeUserId(prefs, any()) } just Runs

        // Act
        viewModel.emailChanged(expectedEmail)
        viewModel.loginChanged(expectedLogin)
        viewModel.password1Changed(expectedPassword)
        viewModel.password2Changed(expectedPasswordControl)
        viewModel.registerUserWithEmail(navigateMock)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        coVerify {
            authRepo.firebaseSignUp(
                email = expectedEmail, password = expectedPassword, login = expectedLogin
            )
        }
        coVerify { authRepo.getUserId() }
        coVerify { prefs.saveData(PreferencesManager.CURRENT_USER, mockUserId) }
        coVerify { usersRepo.saveUserData(userId = mockUserId, userData = mockUserData) }
        coVerify { recipesRepo.createNewCollection() }
        verify { writeUserId(prefs, mockUserId) }
        verify { navigateMock(BottomScreen.HomeScreen.route) }
        assert(!viewModel.loading.value)
    }

    @Test
    fun `test unsuccessful registerUserWithEmail sets error`() = runTest {
        // Arrange
        val expectedEmail = "test@example.com"
        val expectedLogin = "test"
        val expectedPassword = "password"
        val expectedPasswordControl = "password"

        val navigateMock: (String) -> Unit = mockk(relaxed = true)
        val registerException = Exception("Wrong login or password!")

        coEvery { authRepo.firebaseSignUp(any(), any(), any()) } returns flow {
            emit(Response.Loading)
            emit(Response.Failure(registerException))
        }

        // Act
        viewModel.emailChanged(expectedEmail)
        viewModel.loginChanged(expectedLogin)
        viewModel.password1Changed(expectedPassword)
        viewModel.password2Changed(expectedPasswordControl)
        viewModel.registerUserWithEmail(navigateMock)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        coVerify {
            authRepo.firebaseSignUp(
                email = expectedEmail, password = expectedPassword, login = expectedLogin
            )
        }
        assert(viewModel.errorMessage.value == registerException) {
            "Expected ${registerException.message}, but was ${viewModel.errorMessage.value?.message}"
        }
        assert(!viewModel.loading.value)
    }
}