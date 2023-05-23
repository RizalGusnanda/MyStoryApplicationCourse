package com.example.mystoryapplicationcourses.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapplicationcourses.data.utils.RegisterResponse
import com.example.mystoryapplicationcourses.data.utils.respositor.AuthRepositor
import com.example.mystoryapplicationcourses.settings.getOrAwaitValue
import com.example.mystoryapplicationcourses.ui.auth.Register.RegisterViewModel
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import com.example.mystoryapplicationcourses.data.utils.respositor.Result
import com.example.mystoryapplicationcourses.settings.DataDummy
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: AuthRepositor

    private lateinit var signUpViewModel: RegisterViewModel
    private val dummy = DataDummy()
    private val dummySignUpResponse = dummy.generateDummyRegister()

    private val name = "miku"
    private val email = "supersss@gmail.com"
    private val password = "12345678"

    @Before
    fun setUp() {
        signUpViewModel = RegisterViewModel(userRepository)
    }

    private fun setupRegisterScenario(expectedResponse: Result<RegisterResponse>) {
        val liveDataResponse = MutableLiveData<Result<RegisterResponse>>()
        liveDataResponse.value = expectedResponse

        Mockito.`when`(userRepository.register(name, email, password)).thenReturn(liveDataResponse)
    }

    @Test
    fun `when register Should Not Null and return success`() {
        setupRegisterScenario(Result.Success(dummySignUpResponse))

        val actualResponse = signUpViewModel.saveUser(name, email, password).getOrAwaitValue()
        Mockito.verify(userRepository).register(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        setupRegisterScenario(Result.Error("network error"))

        val actualResponse = signUpViewModel.saveUser(name, email, password).getOrAwaitValue()
        Mockito.verify(userRepository).register(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}
