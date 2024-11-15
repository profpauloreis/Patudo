package com.example.patudo.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.example.patudo.R
import com.example.patudo.databinding.ActivityDadosCaninoBinding

class DadosCaninoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDadosCaninoBinding
    private var modoEdicao = false

    // Variáveis para armazenar dados originais
    private var dadosOriginais = mutableMapOf<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDadosCaninoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Carregar dados do SharedPreferences
        carregarDados()

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        // Configurar a Toolbar
        setSupportActionBar(binding.toolbar)
        // Remover o título padrão
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // setupToolbar()
        setupInitialState()
        setupListeners()

        // Configurar o DatePicker
        binding.editTextDataNascimento.setOnClickListener {
            showDatePickerDialog()
        }

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

        // Configurar DatePicker
        setupDatePicker()
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


    private fun setupDatePicker() {
        binding.editTextDataNascimento.setOnClickListener {
            if (modoEdicao) {
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            binding.editTextDataNascimento.setText(selectedDate)
        }, year, month, day).show()
    }

    private fun setEditingEnabled(enabled: Boolean) {
        binding.apply {
            editTextRaca.isEnabled = enabled
            editTextDataNascimento.isEnabled = enabled
            editTextCor.isEnabled = enabled

            // RadioGroups
            radioGroupSexo.children.forEach { it.isEnabled = enabled }
            radioGroupCauda.children.forEach { it.isEnabled = enabled }

            // Checkboxes da pelagem
            checkComprida.isEnabled = enabled
            checkMedia.isEnabled = enabled
            checkCurta.isEnabled = enabled
            checkLisa.isEnabled = enabled
            checkEncaracolada.isEnabled = enabled
            checkOndulada.isEnabled = enabled
            checkCerdosa.isEnabled = enabled
        }
    }

    private fun iniciarModoEdicao() {
        modoEdicao = true
        setEditingEnabled(true)
        binding.apply {
            btnEditar.text = "Guardar"
            btnEditar.setTextColor(ContextCompat.getColor(this@DadosCaninoActivity, R.color.white))
            btnEditar.setBackgroundColor(ContextCompat.getColor(this@DadosCaninoActivity, R.color.black))
            btnCancelar.visibility = View.VISIBLE
            btnVoltar.visibility = View.GONE
        }
    }

    private fun finalizarModoEdicao() {
        modoEdicao = false
        setEditingEnabled(false)
        binding.apply {
            btnEditar.text = "Editar"
            btnEditar.setTextColor(ContextCompat.getColor(this@DadosCaninoActivity, R.color.white))
            btnEditar.setBackgroundColor(ContextCompat.getColor(this@DadosCaninoActivity, R.color.black))
            btnCancelar.visibility = View.GONE
            btnVoltar.visibility = View.VISIBLE
        }
    }

    private fun salvarEstadoAtual() {
        binding.apply {
            dadosOriginais["raca"] = editTextRaca.text.toString()
            dadosOriginais["dataNascimento"] = editTextDataNascimento.text.toString()
            dadosOriginais["cor"] = editTextCor.text.toString()
            dadosOriginais["sexoId"] = radioGroupSexo.checkedRadioButtonId
            dadosOriginais["caudaId"] = radioGroupCauda.checkedRadioButtonId
            dadosOriginais["pelagemComprida"] = checkComprida.isChecked
            dadosOriginais["pelagemMedia"] = checkMedia.isChecked
            dadosOriginais["pelagemCurta"] = checkCurta.isChecked
            dadosOriginais["pelagemLisa"] = checkLisa.isChecked
            dadosOriginais["pelagemEncaracolada"] = checkEncaracolada.isChecked
            dadosOriginais["pelagemOndulada"] = checkOndulada.isChecked
            dadosOriginais["pelagemCerdosa"] = checkCerdosa.isChecked
        }
    }

    private fun restaurarEstadoOriginal() {
        binding.apply {
            editTextRaca.setText(dadosOriginais["raca"] as String)
            editTextDataNascimento.setText(dadosOriginais["dataNascimento"] as String)
            editTextCor.setText(dadosOriginais["cor"] as String)
            radioGroupSexo.check(dadosOriginais["sexoId"] as Int)
            radioGroupCauda.check(dadosOriginais["caudaId"] as Int)
            checkComprida.isChecked = dadosOriginais["pelagemComprida"] as Boolean
            checkMedia.isChecked = dadosOriginais["pelagemMedia"] as Boolean
            checkCurta.isChecked = dadosOriginais["pelagemCurta"] as Boolean
            checkLisa.isChecked = dadosOriginais["pelagemLisa"] as Boolean
            checkEncaracolada.isChecked = dadosOriginais["pelagemEncaracolada"] as Boolean
            checkOndulada.isChecked = dadosOriginais["pelagemOndulada"] as Boolean
            checkCerdosa.isChecked = dadosOriginais["pelagemCerdosa"] as Boolean
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

    private fun guardarDados() {
        val sharedPreferences = this.getSharedPreferences("dados_canino", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Salvar os dados no SharedPreferences
        editor.putString("raca", binding.editTextRaca.text.toString())
        editor.putString("dataNascimento", binding.editTextDataNascimento.text.toString())
        editor.putString("cor", binding.editTextCor.text.toString())
        editor.putInt("sexoId", binding.radioGroupSexo.checkedRadioButtonId)
        editor.putInt("caudaId", binding.radioGroupCauda.checkedRadioButtonId)
        editor.putBoolean("pelagemComprida", binding.checkComprida.isChecked)
        editor.putBoolean("pelagemMedia", binding.checkMedia.isChecked)
        editor.putBoolean("pelagemCurta", binding.checkCurta.isChecked)
        editor.putBoolean("pelagemLisa", binding.checkLisa.isChecked)
        editor.putBoolean("pelagemEncaracolada", binding.checkEncaracolada.isChecked)
        editor.putBoolean("pelagemOndulada", binding.checkOndulada.isChecked)
        editor.putBoolean("pelagemCerdosa", binding.checkCerdosa.isChecked)

        editor.apply()
        Toast.makeText(this, "Dados guardados com sucesso!", Toast.LENGTH_SHORT).show()
    }

    private fun carregarDados() {
        val sharedPreferences = this.getSharedPreferences("dados_canino", Context.MODE_PRIVATE)

        // Carregar dados dos campos
        binding.editTextRaca.setText(sharedPreferences.getString("raca", ""))
        binding.editTextDataNascimento.setText(sharedPreferences.getString("dataNascimento", ""))
        binding.editTextCor.setText(sharedPreferences.getString("cor", ""))
        binding.radioGroupSexo.check(sharedPreferences.getInt("sexoId", -1))
        binding.radioGroupCauda.check(sharedPreferences.getInt("caudaId", -1))
        binding.checkComprida.isChecked = sharedPreferences.getBoolean("pelagemComprida", false)
        binding.checkMedia.isChecked = sharedPreferences.getBoolean("pelagemMedia", false)
        binding.checkCurta.isChecked = sharedPreferences.getBoolean("pelagemCurta", false)
        binding.checkLisa.isChecked = sharedPreferences.getBoolean("pelagemLisa", false)
        binding.checkEncaracolada.isChecked = sharedPreferences.getBoolean("pelagemEncaracolada", false)
        binding.checkOndulada.isChecked = sharedPreferences.getBoolean("pelagemOndulada", false)
        binding.checkCerdosa.isChecked = sharedPreferences.getBoolean("pelagemCerdosa", false)
    }

    override fun onSupportNavigateUp(): Boolean {

        return true
    }

}