package io.bolttech.coverdemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun testLoginSuccessful() = runBlockingTest {
        viewModel.onEmailChange("user@example.com")
        viewModel.onPasswordChange("password")
        viewModel.login()

        assertTrue(viewModel.isLoading.value)

        testDispatcher.scheduler.apply { advanceTimeBy(2000); runCurrent() }

        assertFalse(viewModel.isLoading.value)
        assertTrue(viewModel.loginSuccessful.value!!)
    }

    @Test
    fun testLoginFailed() = runBlockingTest {
        viewModel.onEmailChange("user@example.com")
        viewModel.onPasswordChange("wrongpassword")
        viewModel.login()

        assertTrue(viewModel.isLoading.value)

        testDispatcher.scheduler.apply { advanceTimeBy(2000); runCurrent() }

        assertFalse(viewModel.isLoading.value)
        assertFalse(viewModel.loginSuccessful.value!!)
    }

    @Test
    fun testInitialState() {
        assertEquals("user@example.com", viewModel.email.value)
        assertEquals("password", viewModel.password.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.loginSuccessful.value)
    }

    @Test
    fun testOnEmailChange() {
        val newEmail = "new@example.com"
        viewModel.onEmailChange(newEmail)
        assertEquals(newEmail, viewModel.email.value)
    }

    @Test
    fun testOnPasswordChange() {
        val newPassword = "newpassword"
        viewModel.onPasswordChange(newPassword)
        assertEquals(newPassword, viewModel.password.value)
    }
}
