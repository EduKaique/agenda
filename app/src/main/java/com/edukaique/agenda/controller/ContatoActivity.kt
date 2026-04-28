package com.edukaique.agenda.controller

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edukaique.agenda.R
import com.edukaique.agenda.dao.ContatoDAO
import com.edukaique.agenda.model.Contato

class ContatoActivity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var etTelefone: EditText
    private lateinit var spCategoria: Spinner
    private lateinit var dao: ContatoDAO
    private var idContato = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)

        dao = ContatoDAO(this)
        etNome = findViewById(R.id.etNome)
        etTelefone = findViewById(R.id.etTelefone)
        spCategoria = findViewById(R.id.spCategoria)
        val btnSalvar: Button = findViewById(R.id.btnSalvar)
        val btnExcluir: Button = findViewById(R.id.btnExcluir)

        val categorias = arrayOf("Amigos", "Família", "Trabalho", "Outros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        spCategoria.adapter = adapter

        if (intent.hasExtra("ID_CONTATO")) {
            idContato = intent.getIntExtra("ID_CONTATO", -1)
            etNome.setText(intent.getStringExtra("NOME"))
            etTelefone.setText(intent.getStringExtra("FONE"))
            btnExcluir.visibility = View.VISIBLE
        }

        btnSalvar.setOnClickListener { salvar() }
        btnExcluir.setOnClickListener {
            dao.deletar(idContato)
            finish()
        }
    }

    private fun salvar() {
        val nome = etNome.text.toString().trim()
        val fone = etTelefone.text.toString().trim()
        val cat = spCategoria.selectedItem.toString()

        if (nome.isEmpty()) {
            Toast.makeText(this, "Preencha o nome!", Toast.LENGTH_SHORT).show()
            etNome.requestFocus() 
            return
        }

        if (fone.isEmpty()) {
            Toast.makeText(this, "Preencha o telefone!", Toast.LENGTH_SHORT).show()
            etTelefone.requestFocus() 
            return
        }

        val contato = Contato(nome, fone, cat)
        if (idContato == -1) {
            dao.inserir(contato)
        } else {
            contato.id = idContato
            dao.atualizar(contato)
        }
        finish()
    }
}