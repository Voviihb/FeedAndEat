package com.vk_edu.feed_and_eat.login_test

import com.google.firebase.auth.AuthResult
import com.vk_edu.feed_and_eat.MainDispatcherRule
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.pres.LoginScreenViewModel
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
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
class LoginScreenViewModelTest {
    private lateinit var viewModel: LoginScreenViewModel
    private lateinit var repo: AuthRepoImpl
    private lateinit var prefs: PreferencesManager

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repo = mockk()
        prefs = mockk()
        viewModel = LoginScreenViewModel(repo, prefs)
    }

    @Test
    fun `test loginForm updates after changes`() = runTest {
        // Arrange
        val expectedEmail = "test@example.com"
        val expectedPassword = "password"

        // Act
        viewModel.emailChanged(expectedEmail)
        viewModel.passwordChanged(expectedPassword)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        assert(viewModel.loginFormState.value.email == expectedEmail)
        assert(viewModel.loginFormState.value.password == expectedPassword)
    }

    @Test
    fun `loginWithEmail success navigates to HomeScreen`() = runTest {
        // Arrange
        val mockUserId = "test_user_id"
        val mockAuthResult: AuthResult = mockk()
        val navigateMock: (String) -> Unit = mockk(relaxed = true)

        coEvery { repo.firebaseSignIn(any(), any()) } returns flow {
            emit(Response.Loading)
            emit(Response.Success(mockAuthResult))
        }
        coEvery { repo.getUserId() } returns mockUserId
        every { prefs.saveData(any(), any()) } just Runs

        // Act
        viewModel.emailChanged("test@example.com")
        viewModel.passwordChanged("password")
        viewModel.loginWithEmail(navigateMock)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        coVerify { repo.firebaseSignIn("test@example.com", "password") }
        coVerify { repo.getUserId() }
        coVerify { prefs.saveData(PreferencesManager.CURRENT_USER, mockUserId) }
        verify { navigateMock(BottomScreen.HomeScreen.route) }
        assert(!viewModel.loading.value)
    }

    @Test
    fun `test unsuccessful loginWithEmail sets error`() = runTest {
        // Arrange
        val navigateMock: (String) -> Unit = mockk(relaxed = true)
        val loginException = Exception("Wrong login or password!")

        coEvery { repo.firebaseSignIn(any(), any()) } returns flow {
            emit(Response.Loading)
            emit(Response.Failure(loginException))
        }

        // Act
        viewModel.emailChanged("test@example.com")
        viewModel.passwordChanged("password")
        viewModel.loginWithEmail(navigateMock)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        coVerify { repo.firebaseSignIn("test@example.com", "password") }
        assert(viewModel.errorMessage.value == loginException) {
            "Expected ${loginException.message}, but was ${viewModel.errorMessage.value?.message}"
        }
        assert(!viewModel.loading.value)
    }

    @Test
    fun `signInAnonymously success navigates to HomeScreen`() = runTest {
        // Arrange
        val mockUserId = "test_user_id"
        val mockAuthResult: AuthResult = mockk()
        val navigateMock: (String) -> Unit = mockk(relaxed = true)

        coEvery { repo.firebaseSignInAnonymously() } returns flow {
            emit(Response.Loading)
            emit(Response.Success(mockAuthResult))
        }
        coEvery { repo.getUserId() } returns mockUserId
        every { prefs.saveData(any(), any()) } just Runs

        // Act
        viewModel.signInAnonymously(navigateMock)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        coVerify { repo.firebaseSignInAnonymously() }
        coVerify { repo.getUserId() }
        coVerify { prefs.saveData(PreferencesManager.CURRENT_USER, mockUserId) }
        verify { navigateMock(BottomScreen.HomeScreen.route) }
        assert(!viewModel.loading.value)
    }

    @Test
    fun `test unsuccessful signInAnonymously sets error`() = runTest {
        // Arrange
        val navigateMock: (String) -> Unit = mockk(relaxed = true)
        val loginException = Exception("Unexpected error!")

        coEvery { repo.firebaseSignInAnonymously() } returns flow {
            emit(Response.Loading)
            emit(Response.Failure(loginException))
        }

        // Act
        viewModel.signInAnonymously(navigateMock)
        advanceUntilIdle() // Wait until all coroutines finish their work

        // Assert
        coVerify { repo.firebaseSignInAnonymously() }
        assert(viewModel.errorMessage.value == loginException) {
            "Expected ${loginException.message}, but was ${viewModel.errorMessage.value?.message}"
        }
        assert(!viewModel.loading.value)
    }
}