package com.example.patudo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.patudo.R
import com.example.patudo.database.DBHelper
import com.example.patudo.databinding.ActivityContatosBinding
import com.example.patudo.model.ContactModel

class ContatosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContatosBinding
    private lateinit var contactList: ArrayList<ContactModel>
    private lateinit var adapter: ArrayAdapter<ContactModel>
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var dbHelper: DBHelper

    // Variável para controlar a ordem de ordenação
    private var isAscendingOrder = true

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContatosBinding.inflate(layoutInflater)
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


        dbHelper = DBHelper(this)
        contactList = dbHelper.getAllContatos()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.contactList)
        binding.listViewContatos.adapter = adapter

        // Tratamento de cliques nos itens
        binding.listViewContatos.setOnItemClickListener { _, _, position, _ ->
            val contact = contactList[position]
            //Toast.makeText(this, "Clicou em ${contact.nome}", Toast.LENGTH_SHORT).show()
            // Abrir activity/dialog para editar contato
            val intent = Intent(applicationContext, ContatoDetalheActivity::class.java)
            intent.putExtra("id", contactList[position].id)
            //startActivity(intent)
            result.launch(intent)
        }

        // Botão de adicionar
        binding.buttonAdd.setOnClickListener {
            // Abrir activity/dialog para adicionar contato
            val intent = Intent(applicationContext, NovoContatoActivity::class.java)
            result.launch(intent)
        }
        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                contactList = dbHelper.getAllContatos()
                //adapter.notifyDataSetChanged()
                loadLista()
            } else if (it.resultCode == RESULT_CANCELED && it.data != null) {
                Toast.makeText(applicationContext, "Operação cancelada.", Toast.LENGTH_LONG).show()
            }
        }


        // Botão de ordenação
        binding.buttonOrder.setOnClickListener {
            // Alternar entre ordenação ascendente e descendente
            if (isAscendingOrder) {
                contactList.sortByDescending { it.nome }
                binding.buttonOrder.setIconResource(R.drawable.baseline_arrow_downward_24)
            } else {
                contactList.sortBy { it.nome }
                binding.buttonOrder.setIconResource(R.drawable.baseline_arrow_upward_24)
            }
            isAscendingOrder = !isAscendingOrder // Alternar o estado
            adapter.notifyDataSetChanged()
        }

        // Atualizar lista
        fun updateList() {
            contactList.clear()
            contactList.addAll(dbHelper.getAllContatos())
            adapter.notifyDataSetChanged()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun loadLista() {
        contactList = dbHelper.getAllContatos()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.contactList)
        binding.listViewContatos.adapter = adapter
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

}