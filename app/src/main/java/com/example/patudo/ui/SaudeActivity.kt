package com.example.patudo.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.databinding.ActivitySaudeBinding

class SaudeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaudeBinding
    private var modoEdicao = false
    private var dadosOriginais = mutableMapOf<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySaudeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        carregarDados()

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        // Configurar a Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupInitialState()
        setupListeners()

        binding.editTextDataVacina.setOnClickListener {
            showDatePickerDialog(binding.editTextDataVacina)
        }

        binding.editTextDataMedicacao.setOnClickListener {
            showDatePickerDialog(binding.editTextDataMedicacao)
        }

        binding.editTextDataProtecao.setOnClickListener {
            showDatePickerDialog(binding.editTextDataProtecao)
        }

        binding.editTextDataGrooming.setOnClickListener {
            showDatePickerDialog(binding.editTextDataGrooming)
        }

    }

    private fun carregarDados() {
        val sharedPreferences = this.getSharedPreferences("saude_canino", Context.MODE_PRIVATE)
        binding.editTextDataVacina.setText(sharedPreferences.getString("dataVacina", ""))
        binding.editTextDataMedicacao.setText(sharedPreferences.getString("dataMedicacao", ""))
        binding.editTextDataProtecao.setText(sharedPreferences.getString("dataProtecao", ""))
        binding.editTextDataGrooming.setText(sharedPreferences.getString("dataGrooming", ""))
        binding.editTextAnotacoes.setText(sharedPreferences.getString("anotacoes", ""))
    }

    private fun guardarDados() {
        val sharedPreferences = this.getSharedPreferences("saude_canino", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("dataVacina", binding.editTextDataVacina.text.toString())
        editor.putString("dataMedicacao", binding.editTextDataMedicacao.text.toString())
        editor.putString("dataProtecao", binding.editTextDataProtecao.text.toString())
        editor.putString("dataGrooming", binding.editTextDataGrooming.text.toString())
        editor.putString("anotacoes", binding.editTextAnotacoes.text.toString())
        editor.apply()
        Toast.makeText(this, "Dados guardados com sucesso!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Ação para configurações
                startActivity(Intent(this, ConfiguracaoActivity::class.java))
                Toast.makeText(this, "Configurações", Toast.LENGTH_LONG).show()
                true
            }

            R.id.action_about -> {
                // Ação para sobre
                startActivity(Intent(this, SobreActivity::class.java))
                Toast.makeText(this, "Sobre", Toast.LENGTH_LONG).show()
                true
            }

            R.id.action_logout -> {
                // Ação para logout
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupInitialState() {
        // Desativar edição inicialmente
        setEditingEnabled(false)

        // Guardar estado inicial
        salvarEstadoAtual()

    }

    private fun setupListeners() {
        binding.apply {
            btnEditar.setOnClickListener {
                if (!modoEdicao) {
                    iniciarModoEdicao()
                } else {
                    confirmarSalvar()
                }
            }

            btnCancelar.setOnClickListener {
                confirmarCancelar()
            }
        }
    }

    private fun setEditingEnabled(enabled: Boolean) {
        binding.apply {
            editTextDataVacina.isEnabled = enabled
            editTextDataMedicacao.isEnabled = enabled
            editTextDataProtecao.isEnabled = enabled
            editTextDataGrooming.isEnabled = enabled
            editTextAnotacoes.isEnabled = enabled
        }
    }


    private fun iniciarModoEdicao() {
        modoEdicao = true
        setEditingEnabled(true)
        binding.apply {
            btnEditar.text = "Guardar"
            btnEditar.setTextColor(ContextCompat.getColor(this@SaudeActivity, R.color.white))
            btnEditar.setBackgroundColor(ContextCompat.getColor(this@SaudeActivity, R.color.black))
            btnCancelar.visibility = View.VISIBLE
            btnVoltar.visibility = View.GONE
        }
    }

    private fun finalizarModoEdicao() {
        modoEdicao = false
        setEditingEnabled(false)
        binding.apply {
            btnEditar.text = "Editar"
            btnEditar.setTextColor(ContextCompat.getColor(this@SaudeActivity, R.color.white))
            btnEditar.setBackgroundColor(ContextCompat.getColor(this@SaudeActivity, R.color.black))
            btnCancelar.visibility = View.GONE
            btnVoltar.visibility = View.VISIBLE
        }
    }

    private fun salvarEstadoAtual() {
        binding.apply {
            dadosOriginais["dataVacina"] = editTextDataVacina.text.toString()
            dadosOriginais["dataMedicacao"] = editTextDataMedicacao.text.toString()
            dadosOriginais["dataProtecao"] = editTextDataProtecao.text.toString()
            dadosOriginais["dataGrooming"] = editTextDataGrooming.text.toString()
            dadosOriginais["anotacoes"] = editTextAnotacoes.text.toString()
        }
    }

    private fun restaurarEstadoOriginal() {
        binding.apply {
            editTextDataVacina.setText(dadosOriginais["dataVacina"] as String)
            editTextDataMedicacao.setText(dadosOriginais["dataMedicacao"] as String)
            editTextDataProtecao.setText(dadosOriginais["dataProtecao"] as String)
            editTextDataGrooming.setText(dadosOriginais["dataGrooming"] as String)
            editTextAnotacoes.setText(dadosOriginais["anotacoes"] as String)
        }
    }

    private fun confirmarSalvar() {
        AlertDialog.Builder(this)
            .setTitle("Guardar alterações")
            .setMessage("Deseja guardar as alterações realizadas?")
            .setPositiveButton("Sim") { _, _ ->
                guardarDados()
                finalizarModoEdicao()
                salvarEstadoAtual()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun confirmarCancelar() {
        AlertDialog.Builder(this)
            .setTitle("Cancelar edição")
            .setMessage("Tem certeza que deseja cancelar as alterações?")
            .setPositiveButton("Sim") { _, _ ->
                restaurarEstadoOriginal()
                finalizarModoEdicao()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(selectedDate) // usa o EditText passado como parâmetro
        }, year, month, day).show()
    }
}