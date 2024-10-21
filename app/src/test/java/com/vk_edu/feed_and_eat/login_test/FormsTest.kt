package com.vk_edu.feed_and_eat.login_test

import com.vk_edu.feed_and_eat.features.login.pres.LoginForm
import com.vk_edu.feed_and_eat.features.login.pres.RegisterForm
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test

class FormsTest {
    private lateinit var loginForm: LoginForm
    private lateinit var registerForm: RegisterForm

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // LoginForm
    @Test
    fun `test empty constructor for LoginForm`() {
        loginForm = LoginForm()
        val email = loginForm.email
        val password = loginForm.password
        assert(email == "") { "Expected empty string, but was $email" }
        assert(password == "") { "Expected empty string, but was $password" }
    }

    @Test
    fun `test setting empty values to LoginForm`() {
        loginForm = LoginForm("", "")
        val email = loginForm.email
        val password = loginForm.password
        assert(email == "") { "Expected empty string, but was $email" }
        assert(password == "") { "Expected empty string, but was $password" }
    }

    @Test
    fun `test setting email and password values to LoginForm`() {
        val expectedEmail = "mail@mail.ru"
        val expectedPassword = "123456"
        loginForm = LoginForm(expectedEmail, expectedPassword)
        val email = loginForm.email
        val password = loginForm.password
        assert(email == expectedEmail) { "Expected $expectedEmail, but was $email" }
        assert(password == expectedPassword) { "Expected $expectedPassword, but was $password" }
    }

    // RegisterForm
    @Test
    fun `test empty constructor for RegisterForm`() {
        registerForm = RegisterForm()
        val email = registerForm.email
        val login = registerForm.login
        val password = registerForm.password
        val passwordControl = registerForm.passwordControl
        assert(email == "") { "Expected empty string, but was $email" }
        assert(login == "") { "Expected empty string, but was $login" }
        assert(password == "") { "Expected empty string, but was $password" }
        assert(passwordControl == "") { "Expected empty string, but was $passwordControl" }

    }

    @Test
    fun `test setting empty values to RegisterForm`() {
        registerForm = RegisterForm("", "", "", "")
        val email = registerForm.email
        val login = registerForm.login
        val password = registerForm.password
        val passwordControl = registerForm.passwordControl
        assert(email == "") { "Expected empty string, but was $email" }
        assert(login == "") { "Expected empty string, but was $login" }
        assert(password == "") { "Expected empty string, but was $password" }
        assert(passwordControl == "") { "Expected empty string, but was $passwordControl" }
    }

    @Test
    fun `test setting non-empty values to RegisterForm`() {
        val expectedEmail = "mail@mail.ru"
        val expectedLogin = "user"
        val expectedPassword = "123456"
        val expectedPasswordControl = "123456"
        registerForm =
            RegisterForm(expectedEmail, expectedLogin, expectedPassword, expectedPasswordControl)
        val email = registerForm.email
        val login = registerForm.login
        val password = registerForm.password
        val passwordControl = registerForm.passwordControl
        assert(email == expectedEmail) { "Expected $expectedEmail, but was $email" }
        assert(login == expectedLogin) { "Expected $expectedLogin, but was $login" }
        assert(password == expectedPassword) { "Expected $expectedPassword, but was $password" }
        assert(passwordControl == expectedPasswordControl) { "Expected $expectedPasswordControl, but was $passwordControl" }
    }
}