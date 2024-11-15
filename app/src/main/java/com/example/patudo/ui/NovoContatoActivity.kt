package com.example.patudo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.database.DBHelper
import com.example.patudo.databinding.ActivityNovoContatoBinding
import com.example.patudo.model.ContactModel

class NovoContatoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNovoContatoBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var id: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNovoContatoBinding.inflate(layoutInflater)
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

        val db = DBHelper(applicationContext)
        val i = intent

        binding.btnGuardar.setOnClickListener {
            val nome = binding.editNome.text.toString()
            val endereco = binding.editEndereco.text.toString()
            val email = binding.editEmail.text.toString()
            val telefone = binding.editTelefone.text.toString().toInt()
            var imageId = -1
            if (id != null) {
                imageId = id as Int
            }

            var contact = ContactModel(nome = nome, endereco = endereco, email = email, telefone = telefone, imageId = imageId)

            if (nome.isNotEmpty() && endereco.isNotEmpty() && email.isNotEmpty() && telefone.toString().isNotEmpty()) {
                val res = db.inserirContato(contact)
                if (res > 0) {
                    Toast.makeText(applicationContext, "Contato guardado com sucesso.", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK, i)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Erro ao inserir o novo contato.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCancelar.setOnClickListener {
            setResult(RESULT_CANCELED, i)
            finish()
        }

        binding.profileImage.setOnClickListener {
            launcher.launch(Intent(applicationContext, ContatoImagemActivity::class.java))
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                id = it.data?.extras?.getInt("id", 0)
                binding.profileImage.setImageDrawable(resources.getDrawable(id!!))
        } else {
                id = -1
                binding.profileImage.setImageResource(R.drawable.profiledefault)
            }
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
}