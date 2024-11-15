package com.example.patudo.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Configurar aparência dos EditText
        listOf(binding.pin1, binding.pin2, binding.pin3, binding.pin4).forEach { editText ->
            editText.apply {
                setBackgroundResource(R.drawable.edit_text_background) // se estiver usando o background personalizado
                // ou
                // setBackgroundColor(Color.WHITE) // se quiser apenas cor sólida
                setTextColor(Color.BLACK)
                // Opcional: configurar tamanho do texto
                textSize = 32f // tamanho em sp
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar o SharedPreferences
        sharedPreferences = getSharedPreferences("PatudoPrefs", Context.MODE_PRIVATE)

        // Configurar os TextWatchers com navegação automática
        setupPinEditText(binding.pin1, binding.pin2)
        setupPinEditText(binding.pin2, binding.pin3)
        setupPinEditText(binding.pin3, binding.pin4)
        setupPinEditText(binding.pin4, null)

        // Adicionar a lógica para apagar e voltar ao edittext anterior
        setupPinBackspace(binding.pin2, binding.pin1)
        setupPinBackspace(binding.pin3, binding.pin2)
        setupPinBackspace(binding.pin4, binding.pin3)

        /* Configurar o botão de login */
        binding.btnLogin.setOnClickListener {
            // Captura o PIN inserido
            val pin = "${binding.pin1.text}${binding.pin2.text}${binding.pin3.text}${binding.pin4.text}"

            // Verifica se o PIN tem 4 dígitos
            if (pin.length == 4) {
                // Recupera o PIN armazenado
                val sharedPreferences = getSharedPreferences("PatudoPrefs", Context.MODE_PRIVATE)
                val savedPin = sharedPreferences.getString("savedPin", null)

                // Se não houver PIN armazenado, define o PIN padrão
                if (savedPin == null) {
                    // Define o PIN padrão como "1234"
                    val editor = sharedPreferences.edit()
                    editor.putString("savedPin", "1234")
                    editor.apply()
                    Toast.makeText(this, "PIN padrão definido como 1234.", Toast.LENGTH_SHORT).show()

                    // Verifica se o PIN inserido é o padrão
                    if (pin == "1234") {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        limpar()
                        Toast.makeText(this, "PIN incorreto!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Se já houver um PIN armazenado, verifica o PIN inserido
                    //Log.d("LoginActivity", "PIN armazenado: $savedPin")
                    if (pin == savedPin) {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        limpar()
                        Toast.makeText(this, "PIN incorreto!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, insira um PIN de 4 dígitos.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun savePin(pin: String) {
        val editor = sharedPreferences.edit()
        editor.putString("savedPin", pin)
        editor.apply()
    }

    private fun setupPinEditText(currentEditText: EditText, nextEditText: EditText?) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    // Mover para o próximo EditText se existir
                    nextEditText?.requestFocus()
                }
                // Ocultar o número com asterisco
                if (s != null && s.isNotEmpty()) {
                    currentEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    currentEditText.transformationMethod = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário implementar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Não é necessário implementar
            }
        })
    }

    private fun setupPinBackspace(currentEditText: EditText, previousEditText: EditText) {
        currentEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (currentEditText.text.isEmpty()) {
                    previousEditText.apply {
                        requestFocus()
                        setText("")
                    }
                }
            }
            false
        }
    }

    private fun limpar() {
        binding.pin1.text.clear()
        binding.pin2.text.clear()
        binding.pin3.text.clear()
        binding.pin4.text.clear()
    }
}