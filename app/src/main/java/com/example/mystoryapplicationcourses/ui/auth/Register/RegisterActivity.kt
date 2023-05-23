package com.example.mystoryapplicationcourses.ui.auth.Register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapplicationcourses.data.utils.RegisterResponse
import com.example.mystoryapplicationcourses.data.utils.network.ApiConfig
import com.example.mystoryapplicationcourses.data.utils.respositor.AuthRepositor
import com.example.mystoryapplicationcourses.ui.auth.Login.LoginActivity
import com.example.mystoryapplicationcourses.data.utils.respositor.Result
import com.example.mystoryapplicationcourses.databinding.ActivityRegisterBinding
import com.example.mystoryapplicationcourses.ui.auth.ViewModelFactory


class   RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepositor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepositor(ApiConfig.getApiService(this))
        registerViewModel = ViewModelProvider(this, ViewModelFactory(this))
            .get(RegisterViewModel::class.java)

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Nothing to do here
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Nothing to do here
            }

            override fun afterTextChanged(s: Editable) {
                if (!s.contains("@")) {
                    binding.emailEditText.error = "Email harus mengandung karakter @"
                } else {
                    binding.emailEditText.error = null
                }
            }
        })


        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length < 8) {
                    binding.passwordEditText.error = "Password harus terdiri dari minimal 8 karakter"
                } else {
                    binding.passwordEditText.error = null
                }
            }
        })

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (!email.contains("@")) {
                binding.edRegisterEmail.error = "Email harus memakai @"
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.edRegisterPassword.error = "Password harus memiliki 8 karakter"
                return@setOnClickListener
            }

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            registerViewModel.saveUser(name, email, password).observe(this) { result ->

                when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    processSignUp(result.data)
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
        playAnimation()
        login()
    }

    private fun processSignUp(data: RegisterResponse) {
        if (data.error) {
            Toast.makeText(this@RegisterActivity, "Gagal Sign Up", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this@RegisterActivity,
                "Sign Up berhasil, silahkan login!",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.nameTextView.isInvisible = state
        binding.emailTextView.isInvisible = state
        binding.passwordTextView.isInvisible= state
        binding.nameEditText.isInvisible = state
        binding.emailEditText.isInvisible = state
        binding.passwordEditText.isInvisible = state
        binding.pbCreateSignup.isVisible = state
        binding.btnRegister.isInvisible = state
        binding.edRegisterName.isInvisible = state
        binding.edRegisterEmail.isInvisible = state
        binding.edRegisterPassword.isInvisible = state
        binding.tvLogin.isInvisible = state
    }


    private fun playAnimation() {

            val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(500)
            val nameTextView = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 0f, 1f).setDuration(500)
            val registerTextView = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 0f, 1f).setDuration(500)
            val emailTextView = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA,0f, 1f).setDuration(500)
            val tvlogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 0f, 1f).setDuration(500)

        AnimatorSet().apply {
            playTogether(
                title,
                nameTextView,
                registerTextView,
                emailTextView,
                tvlogin
            )
            startDelay = 500
        }.start()
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}