package com.example.patudo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.databinding.ActivityContatoImagemBinding

class ContatoImagemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContatoImagemBinding
    private lateinit var i: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContatoImagemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar a Toolbar
        setSupportActionBar(binding.toolbar)
        // Remover o título padrão
        supportActionBar?.setDisplayShowTitleEnabled(false)

        i = intent

        binding.profileImage1.setOnClickListener { sendID(R.drawable.profile1) }
        binding.profileImage2.setOnClickListener { sendID(R.drawable.profile2) }
        binding.profileImage3.setOnClickListener { sendID(R.drawable.profile3) }
        binding.profileImage4.setOnClickListener { sendID(R.drawable.profile4) }
        binding.profileImage5.setOnClickListener { sendID(R.drawable.profile5) }
        binding.profileImage6.setOnClickListener { sendID(R.drawable.profile6) }
        binding.btnRemover.setOnClickListener { sendID(R.drawable.profiledefault) }

        binding.btnVoltar.setOnClickListener {
            Toast.makeText(applicationContext, "Operação cancelada", Toast.LENGTH_LONG).show()
            sendID(0)
            finish()
        }

    }

    private fun sendID(id: Int) {
        // Verificar se o ID é válido antes de enviar
        if (id != 0) { // 0 geralmente é um ID inválido
            i.putExtra("id", id)
            setResult(RESULT_OK, i)
        } else {
            // Lidar com o caso de ID inválido, se necessário
        }
        finish()
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

}