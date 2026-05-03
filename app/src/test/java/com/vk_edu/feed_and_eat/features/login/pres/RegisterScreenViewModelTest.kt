package com.vk_edu.feed_and_eat.features.login.pres

import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import com.vk_edu.feed_and_eat.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepo: AuthRepository = mockk(relaxed = true)
    private val usersRepo: UsersRepository = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)

    private lateinit var viewModel: RegisterScreenViewModel

    @Before
    fun setUp() {
        viewModel = RegisterScreenViewModel(authRepo, usersRepo, preferencesManager)
    }

    // -------------------------------------------------------------------------
    // Обновление полей формы
    // -------------------------------------------------------------------------

    @Test
    fun `emailChanged — обновляет email в состоянии формы`() {
        viewModel.emailChanged("new@example.com")

        assertEquals("new@example.com", viewModel.registerFormState.value.email)
    }

    @Test
    fun `loginChanged — обновляет login в состоянии формы`() {
        viewModel.loginChanged("johndoe")

        assertEquals("johndoe", viewModel.registerFormState.value.login)
    }

    @Test
    fun `password1Changed — обновляет password в состоянии формы`() {
        viewModel.password1Changed("pass1234")

        assertEquals("pass1234", viewModel.registerFormState.value.password)
    }

    @Test
    fun `password2Changed — обновляет passwordControl в состоянии формы`() {
        viewModel.password2Changed("pass1234")

        assertEquals("pass1234", viewModel.registerFormState.value.passwordControl)
    }

    @Test
    fun `все поля формы обновляются независимо`() {
        viewModel.emailChanged("a@b.com")
        viewModel.loginChanged("user")
        viewModel.password1Changed("abc")
        viewModel.password2Changed("abc")

        val state = viewModel.registerFormState.value
        assertEquals("a@b.com", state.email)
        assertEquals("user", state.login)
        assertEquals("abc", state.password)
        assertEquals("abc", state.passwordControl)
    }

    // -------------------------------------------------------------------------
    // Несовпадение паролей
    // -------------------------------------------------------------------------

    @Test
    fun `registerUserWithEmail — пароли не совпадают — выставляет PasswordDiffersException`() = runTest {
        viewModel.password1Changed("abc123")
        viewModel.password2Changed("xyz789")

        viewModel.registerUserWithEmail { }

        assertNotNull(viewModel.errorMessage.value)
        assertTrue(
            "Ожидался PasswordDiffersException",
            viewModel.errorMessage.value is PasswordDiffersException
        )
    }

    @Test
    fun `registerUserWithEmail — пароли не совпадают — навигация не происходит`() = runTest {
        viewModel.password1Changed("aaa")
        viewModel.password2Changed("bbb")

        val navigatedRoutes = mutableListOf<String>()
        viewModel.registerUserWithEmail { navigatedRoutes.add(it) }

        assertTrue(
            "Навигация не должна была произойти при несовпадающих паролях",
            navigatedRoutes.isEmpty()
        )
    }

    // -------------------------------------------------------------------------
    // Успешная регистрация
    // -------------------------------------------------------------------------

    @Test
    fun `registerUserWithEmail — совпадающие пароли и ответ Success — навигация происходит`() = runTest {
        viewModel.emailChanged("user@test.com")
        viewModel.loginChanged("testuser")
        viewModel.password1Changed("secret")
        viewModel.password2Changed("secret")

        coEvery { authRepo.signUp(any(), any(), any()) } returns flowOf(
            Response.Loading,
            Response.Success(Unit)
        )

        val navigatedRoutes = mutableListOf<String>()
        viewModel.registerUserWithEmail { navigatedRoutes.add(it) }

        assertTrue(
            "Ожидалась навигация после успешной регистрации",
            navigatedRoutes.isNotEmpty()
        )
    }

    @Test
    fun `registerUserWithEmail — Response Success — loading становится false`() = runTest {
        viewModel.password1Changed("pass")
        viewModel.password2Changed("pass")

        coEvery { authRepo.signUp(any(), any(), any()) } returns flowOf(
            Response.Loading,
            Response.Success(Unit)
        )

        viewModel.registerUserWithEmail { }

        assertEquals(false, viewModel.loading.value)
    }

    // -------------------------------------------------------------------------
    // Ошибка сервера при регистрации
    // -------------------------------------------------------------------------

    @Test
    fun `registerUserWithEmail — Response Failure — errorMessage содержит исключение`() = runTest {
        val error = RuntimeException("Email уже занят")
        viewModel.password1Changed("pass")
        viewModel.password2Changed("pass")

        coEvery { authRepo.signUp(any(), any(), any()) } returns flowOf(
            Response.Loading,
            Response.Failure(error)
        )

        viewModel.registerUserWithEmail { }

        assertNotNull(viewModel.errorMessage.value)
        assertEquals(error, viewModel.errorMessage.value)
    }

    @Test
    fun `registerUserWithEmail — брошенное исключение — errorMessage не null`() = runTest {
        viewModel.password1Changed("pass")
        viewModel.password2Changed("pass")

        coEvery { authRepo.signUp(any(), any(), any()) } throws RuntimeException("Сеть недоступна")

        viewModel.registerUserWithEmail { }

        assertNotNull(viewModel.errorMessage.value)
    }

    // -------------------------------------------------------------------------
    // clearError
    // -------------------------------------------------------------------------

    @Test
    fun `clearError — сбрасывает errorMessage в null после ошибки`() = runTest {
        viewModel.password1Changed("a")
        viewModel.password2Changed("b") // Несовпадающие пароли → ошибка
        viewModel.registerUserWithEmail { }
        assertNotNull(viewModel.errorMessage.value)

        viewModel.clearError()

        assertNull(viewModel.errorMessage.value)
    }
}
