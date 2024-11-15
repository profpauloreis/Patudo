package com.example.patudo.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar a Toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            toolbar.inflateMenu(R.menu.menu_main)
        }

        // mostrar o PopupMenu programaticamente
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_more -> {
                    showPopupMenu(toolbar)
                    true
                }
                else -> false
            }
        }
        carregarDados()

        binding.btnDadosCanino.setOnClickListener {
            startActivity(Intent(this, DadosCaninoActivity::class.java))
        }

        binding.btnContatos.setOnClickListener {
            startActivity(Intent(this, ContatosActivity::class.java))
        }

        binding.btnSaude.setOnClickListener {
            startActivity(Intent(this, SaudeActivity::class.java))
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun showPopupMenu(view: View) {
        //val popupMenu = PopupMenu(this, view)
        val popupMenu = PopupMenu(this, view, Gravity.END, 0, R.style.CustomPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_more, popupMenu.menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    // Ação para configurações
                    Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ConfiguracaoActivity::class.java))
                    true
                }
                R.id.action_about -> {
                    // Ação para sobre
                    Toast.makeText(this, "Sobre", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SobreActivity::class.java))
                    true
                }
                R.id.action_logout -> {
                    // Ação para logout
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun carregarDados() {
        val sharedPreferences = this.getSharedPreferences("saude_canino", Context.MODE_PRIVATE)
        binding.textVacina.setText(
            "Próxima vacina a " + sharedPreferences.getString(
                "dataVacina",
                ""
            )
        )
        binding.textMedicacao.setText(
            "Proxima medicação a " + sharedPreferences.getString(
                "dataMedicacao",
                ""
            )
        )
        binding.textProtecao.setText(
            "Próxima proteção a " + sharedPreferences.getString(
                "dataProtecao",
                ""
            )
        )
        binding.textGrooming.setText(
            "Última estética a " + sharedPreferences.getString(
                "dataGrooming",
                ""
            )
        )
        binding.textAnotacoes.setText("" + sharedPreferences.getString("anotacoes", ""))
    }
}