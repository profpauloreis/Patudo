package com.example.patudo.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

    //NAO FUNCIONA O TOOLBAR PARA EXIBIR OS ICONES E O ESTILO DO MENU
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_overflow -> {
                showPopupMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    /*// Método para mostrar o PopupMenu
    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this, findViewById(R.id.action_overflow))
        popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

        // Remover o item "Mais opções" do PopupMenu
        popupMenu.menu.removeItem(R.id.action_overflow)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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
                else -> false
            }
        }

        popupMenu.show()
    }*/


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