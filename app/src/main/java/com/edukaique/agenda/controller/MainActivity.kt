package com.edukaique.agenda.controller

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.edukaique.agenda.R
import com.edukaique.agenda.dao.ContatoDAO
import com.edukaique.agenda.model.Contato

class MainActivity : AppCompatActivity() {
    private lateinit var lvContatos: ListView
    private lateinit var dao: ContatoDAO
    private lateinit var tvTotal: TextView
    private lateinit var etBusca: EditText
    private lateinit var spFiltroCategoria: Spinner
    private var contatos: List<Contato> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dao = ContatoDAO(this)
        lvContatos = findViewById(R.id.lvContatos)
        tvTotal = findViewById(R.id.tvTotal)
        etBusca = findViewById(R.id.etBusca)
        spFiltroCategoria = findViewById(R.id.spFiltroCategoria)
        val btnNovo: Button = findViewById(R.id.btnNovo)

        val categoriasFiltro = arrayOf("Todas", "Amigos", "Família", "Trabalho", "Outros")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriasFiltro)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFiltroCategoria.adapter = spinnerAdapter

        btnNovo.setOnClickListener {
            startActivity(Intent(this, ContatoActivity::class.java))
        }

        lvContatos.setOnItemClickListener { parent, _, position, _ ->
            val selecionado = parent.getItemAtPosition(position) as Contato
            val intent = Intent(this, ContatoActivity::class.java).apply {
                putExtra("ID_CONTATO", selecionado.id)
                putExtra("NOME", selecionado.nome)
                putExtra("FONE", selecionado.telefone)
                putExtra("CAT", selecionado.categoria)
            }
            startActivity(intent)
        }

        etBusca.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                atualizarLista()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        spFiltroCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                atualizarLista()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }

    private fun atualizarLista() {
        val filtroNome = etBusca.text.toString().trim()
        val filtroCategoria = spFiltroCategoria.selectedItem?.toString() ?: "Todas"

        var listaFiltrada = if (filtroCategoria == "Todas") {
            dao.listarTodos()
        } else {
            dao.listarPorCategoria(filtroCategoria)
        }

        if (filtroNome.isNotEmpty()) {
            listaFiltrada = listaFiltrada.filter { contato ->
                contato?.nome?.contains(filtroNome, ignoreCase = true) ?: false
            }.toMutableList()
        }

        contatos = listaFiltrada.filterNotNull()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
        lvContatos.adapter = adapter

        tvTotal.text = "Total de contatos: ${dao.totalContatos}"
    }
}