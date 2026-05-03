package com.vk_edu.feed_and_eat.features.login.pres

import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import com.vk_edu.feed_and_eat.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepo: AuthRepository = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)

    private lateinit var viewModel: LoginScreenViewModel

    @Before
    fun setUp() {
        viewModel = LoginScreenViewModel(authRepo, preferencesManager)
    }

    // -------------------------------------------------------------------------
    // emailChanged / passwordChanged
    // -------------------------------------------------------------------------

    @Test
    fun `emailChanged — обновляет email в состоянии формы`() {
        viewModel.emailChanged("test@example.com")

        assertEquals("test@example.com", viewModel.loginFormState.value.email)
    }

    @Test
    fun `passwordChanged — обновляет password в состоянии формы`() {
        viewModel.passwordChanged("secret123")

        assertEquals("secret123", viewModel.loginFormState.value.password)
    }

    @Test
    fun `emailChanged и passwordChanged не затирают друг друга`() {
        viewModel.emailChanged("user@test.com")
        viewModel.passwordChanged("qwerty")

        assertEquals("user@test.com", viewModel.loginFormState.value.email)
        assertEquals("qwerty", viewModel.loginFormState.value.password)
    }

    // -------------------------------------------------------------------------
    // loginWithEmail — Success
    // -------------------------------------------------------------------------

    @Test
    fun `loginWithEmail — при Response Success вызывает навигацию`() = runTest {
        coEvery { authRepo.signIn(any(), any()) } returns flowOf(
            Response.Loading,
            Response.Success(Unit)
        )

        val navigatedRoutes = mutableListOf<String>()
        viewModel.loginWithEmail { route -> navigatedRoutes.add(route) }

        assert(navigatedRoutes.isNotEmpty()) {
            "Ожидалась навигация, но navigateToRoute не был вызван"
        }
    }

    @Test
    fun `loginWithEmail — при Response Success loading становится false`() = runTest {
        coEvery { authRepo.signIn(any(), any()) } returns flowOf(
            Response.Loading,
            Response.Success(Unit)
        )

        viewModel.loginWithEmail { }

        assertEquals(false, viewModel.loading.value)
    }

    // -------------------------------------------------------------------------
    // loginWithEmail — Failure
    // -------------------------------------------------------------------------

    @Test
    fun `loginWithEmail — при Response Failure errorMessage содержит исключение`() = runTest {
        val error = RuntimeException("Неверный логин или пароль")
        coEvery { authRepo.signIn(any(), any()) } returns flowOf(
            Response.Loading,
            Response.Failure(error)
        )

        viewModel.loginWithEmail { }

        assertNotNull(viewModel.errorMessage.value)
        assertEquals(error, viewModel.errorMessage.value)
    }

    @Test
    fun `loginWithEmail — при Response Failure loading становится false`() = runTest {
        coEvery { authRepo.signIn(any(), any()) } returns flowOf(
            Response.Failure(RuntimeException("Ошибка"))
        )

        viewModel.loginWithEmail { }

        assertEquals(false, viewModel.loading.value)
    }

    @Test
    fun `loginWithEmail — при брошенном исключении errorMessage содержит исключение`() = runTest {
        val error = RuntimeException("Сетевая ошибка")
        coEvery { authRepo.signIn(any(), any()) } throws error

        viewModel.loginWithEmail { }

        assertNotNull(viewModel.errorMessage.value)
    }

    // -------------------------------------------------------------------------
    // clearError
    // -------------------------------------------------------------------------

    @Test
    fun `clearError — сбрасывает errorMessage в null`() = runTest {
        coEvery { authRepo.signIn(any(), any()) } returns flowOf(
            Response.Failure(RuntimeException("Ошибка"))
        )
        viewModel.loginWithEmail { }
        assertNotNull(viewModel.errorMessage.value)

        viewModel.clearError()

        assertNull(viewModel.errorMessage.value)
    }
}
