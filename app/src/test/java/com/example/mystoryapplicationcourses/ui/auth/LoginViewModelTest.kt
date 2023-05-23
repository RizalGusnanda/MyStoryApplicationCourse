package com.example.mystoryapplicationcourses.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapplicationcourses.data.utils.LoginResponse
import com.example.mystoryapplicationcourses.data.utils.respositor.AuthRepositor
import com.example.mystoryapplicationcourses.settings.getOrAwaitValue
import com.example.mystoryapplicationcourses.ui.auth.Login.LoginViewModel
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.mystoryapplicationcourses.data.utils.respositor.Result
import com.example.mystoryapplicationcourses.settings.DataDummy
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: AuthRepositor

    private lateinit var loginViewModel: LoginViewModel
    private val dummy = DataDummy()
    private val dummyLoginResponse = dummy.generateDummyLogin()

    private val email = "supersss@gmail.com"
    private val password = "12345678"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    private fun setupLoginScenario(expectedResponse: Result<LoginResponse>) {
        val liveDataResponse = MutableLiveData<Result<LoginResponse>>()
        liveDataResponse.value = expectedResponse

        Mockito.`when`(storyRepository.loginUser(email, password)).thenReturn(liveDataResponse)
    }

    @Test
    fun `when login Should Not Null and return success`() {
        setupLoginScenario(Result.Success(dummyLoginResponse))

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).loginUser(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        setupLoginScenario(Result.Error("network error"))

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).loginUser(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}
