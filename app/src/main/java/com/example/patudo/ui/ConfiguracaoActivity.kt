package com.example.patudo.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.databinding.ActivityConfiguracaoBinding

class ConfiguracaoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracaoBinding

    private lateinit var textatualpin1: EditText
    private lateinit var textatualpin2: EditText
    private lateinit var textatualpin3: EditText
    private lateinit var textatualpin4: EditText
    private lateinit var textnovopin1: EditText
    private lateinit var textnovopin2: EditText
    private lateinit var textnovopin3: EditText
    private lateinit var textnovopin4: EditText
    private lateinit var btnAlterarPin: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityConfiguracaoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa os campos de entrada e o botão
        textatualpin1 = findViewById(R.id.textatualpin1)
        textatualpin2 = findViewById(R.id.textatualpin2)
        textatualpin3 = findViewById(R.id.textatualpin3)
        textatualpin4 = findViewById(R.id.textatualpin4)
        textnovopin1 = findViewById(R.id.textnovopin1)
        textnovopin2 = findViewById(R.id.textnovopin2)
        textnovopin3 = findViewById(R.id.textnovopin3)
        textnovopin4 = findViewById(R.id.textnovopin4)
        btnAlterarPin = findViewById(R.id.btnAtualizarPin)

        // Inicializa SharedPreferences
        sharedPreferences = getSharedPreferences("PatudoPrefs", MODE_PRIVATE)

        // Configurar os TextWatchers com navegação automática
        setupPinEditText(binding.textatualpin1, binding.textatualpin2)
        setupPinEditText(binding.textatualpin2, binding.textatualpin3)
        setupPinEditText(binding.textatualpin3, binding.textatualpin4)
        setupPinEditText(binding.textatualpin4, null)

        setupPinEditText(binding.textnovopin1, binding.textnovopin2)
        setupPinEditText(binding.textnovopin2, binding.textnovopin3)
        setupPinEditText(binding.textnovopin3, binding.textnovopin4)
        setupPinEditText(binding.textnovopin4, null)

        // Adicionar a lógica para apagar e voltar ao edittext anterior
        setupPinBackspace(binding.textatualpin2, binding.textatualpin1)
        setupPinBackspace(binding.textatualpin3, binding.textatualpin2)
        setupPinBackspace(binding.textatualpin4, binding.textatualpin3)

        setupPinBackspace(binding.textnovopin2, binding.textnovopin1)
        setupPinBackspace(binding.textnovopin3, binding.textnovopin2)
        setupPinBackspace(binding.textnovopin4, binding.textnovopin3)

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        btnAlterarPin.setOnClickListener {
            // Captura o PIN atual e o novo PIN
            val pinAtual = "${textatualpin1.text}${textatualpin2.text}${textatualpin3.text}${textatualpin4.text}"
            val novoPin = "${textnovopin1.text}${textnovopin2.text}${textnovopin3.text}${textnovopin4.text}"

            // Recupera o PIN atual armazenado
            val pinArmazenado = sharedPreferences.getString("savedPin", null)
            // Log.d("ConfiguracaoActivity", "PIN armazenado: $pinArmazenado")

            // Verifica se o PIN atual está correto
            if (pinArmazenado != null && pinArmazenado == pinAtual) {
                if (novoPin.length == 4) { // Verifica se o novo PIN tem 4 dígitos
                    // Armazena o novo PIN
                    val editor = sharedPreferences.edit()
                    editor.putString("savedPin", novoPin)
                    editor.apply()

                    // Limpa os campos após alteração
                    limpar()
                    Toast.makeText(this, "PIN alterado com sucesso!", Toast.LENGTH_SHORT).show()

                    // Redireciona para a LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    Toast.makeText(this, "O novo PIN deve ter 4 dígitos.", Toast.LENGTH_SHORT).show()
                }
            } else {
                limpar()
                Toast.makeText(this, "O PIN atual está incorreto.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun limpar() {
        textatualpin1.text.clear()
        textatualpin2.text.clear()
        textatualpin3.text.clear()
        textatualpin4.text.clear()
        textnovopin1.text.clear()
        textnovopin2.text.clear()
        textnovopin3.text.clear()
        textnovopin4.text.clear()
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

}