package com.example.patudo.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.database.DBHelper
import com.example.patudo.databinding.ActivityContatoDetalheBinding
import com.example.patudo.model.ContactModel


class ContatoDetalheActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContatoDetalheBinding
    private lateinit var db: DBHelper
    private lateinit var contact: ContactModel
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int? = -1
    private val REQUEST_PHONE_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContatoDetalheBinding.inflate(layoutInflater)
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

        val i = intent
        val id = i.extras?.getInt("id")
        db = DBHelper(applicationContext)
        if (id != null) {
            contact = db.getContato(id)!!
            binding.editNome.setText(contact.nome)
            binding.editEndereco.setText(contact.endereco)
            binding.editEmail.setText(contact.email)
            binding.editTelefone.setText(contact.telefone.toString())
            imageId = contact.imageId
            if (imageId != null && imageId != -1) {
                binding.profileImage.setImageDrawable(ContextCompat.getDrawable(this, imageId!!))
            } else {
                binding.profileImage.setImageResource(R.drawable.profiledefault)
            }
            
        }

        binding.btnCancelar.setOnClickListener {
            binding.editNome.setText(contact.nome)
            binding.editEndereco.setText(contact.endereco)
            binding.editEmail.setText(contact.email)
            binding.editTelefone.setText(contact.telefone.toString())
            if (imageId != null && imageId != -1) {
                //binding.profileImage.setImageDrawable(resources.getDrawable(imageId!!))
                binding.profileImage.setImageDrawable(ContextCompat.getDrawable(this, imageId!!))
            } else {
                binding.profileImage.setImageResource(R.drawable.profiledefault)
            }
        }

        binding.btnGuardar.setOnClickListener {
            val contact = db.getContato(contact.id)

            if (contact != null) {
                // Cria um novo objeto ContactModel com os dados atualizados
                val updatedContact = ContactModel(
                    id = contact.id,
                    nome = binding.editNome.text.toString(),
                    endereco = binding.editEndereco.text.toString(),
                    email = binding.editEmail.text.toString(),
                    telefone = binding.editTelefone.text.toString().toInt(),
                    imageId = imageId ?: contact.imageId // usa o novo imageId, se disponível
                )

                // Chama a função atualizarContato com o objeto atualizado
                val res = db.atualizarContato(updatedContact)

                if (res > 0) {
                    Toast.makeText(applicationContext, "Contato atualizado com sucesso.", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK, i)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Erro ao atualizar o contato.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Contato não encontrado.", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED, i)
                finish()
            }
        }

        binding.btnDelete.setOnClickListener {
            val res = db.removerContato(contact.id)
            if (res > 0) {
                Toast.makeText(applicationContext, "Contato removido com sucesso.", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Erro ao remover o contato.", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED, i)
                finish()
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
                imageId = it.data?.extras?.getInt("id", -1) // Usar -1 como valor padrão
                if (imageId != null && imageId != -1) {
                    try {
                        binding.profileImage.setImageDrawable(ContextCompat.getDrawable(this, imageId!!))
                    } catch (e: Resources.NotFoundException) {
                        binding.profileImage.setImageResource(R.drawable.profiledefault)
                    }
                } else {
                    binding.profileImage.setImageResource(R.drawable.profiledefault)
                }
            } else {
                imageId = -1
                binding.profileImage.setImageResource(R.drawable.profiledefault)
            }
        }

        binding.imageLocation.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                android.net.Uri.parse("geo:0,0?q=" + binding.editEndereco.text.toString()))
            startActivity(intent)
        }

        binding.imageEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contact.email))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contato de Patudo")
            intent.putExtra(Intent.EXTRA_TEXT, "Enviado por Patudo app.")
            try {
                startActivity(Intent.createChooser(intent, "Escolha um aplicativo de email"))
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Erro ao enviar email.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageTelefone.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(applicationContext,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                ) {
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            } else  {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = android.net.Uri.parse("tel:" + binding.editTelefone.text.toString())
                startActivity(intent)
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