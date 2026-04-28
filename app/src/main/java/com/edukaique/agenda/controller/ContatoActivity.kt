package com.edukaique.agenda.controller

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        aplicarMascaraTelefone()

        val categorias = arrayOf("Amigos", "Família", "Trabalho", "Outros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter

        if (intent.hasExtra("ID_CONTATO")) {
            idContato = intent.getIntExtra("ID_CONTATO", -1)
            etNome.setText(intent.getStringExtra("NOME"))
            etTelefone.setText(intent.getStringExtra("FONE"))

            val categoriaSalva = intent.getStringExtra("CAT")
            val posicao = categorias.indexOf(categoriaSalva)
            if (posicao > -1) {
                spCategoria.setSelection(posicao)
            }

            btnExcluir.visibility = View.VISIBLE
        }

        btnSalvar.setOnClickListener { salvar() }

        btnExcluir.setOnClickListener {
            dao.deletar(idContato)
            Toast.makeText(this, "Contato removido com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun aplicarMascaraTelefone() {
        etTelefone.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                var str = s.toString().replace(Regex("[^\\d]"), "")
                if (str.length > 11) str = str.substring(0, 11)

                var mask = ""
                val mascaraPadrao = if (str.length == 11) "(##) #####-####" else "(##) ####-####"
                var i = 0
                for (m in mascaraPadrao) {
                    if (m != '#' && str.length > i) {
                        mask += m
                        continue
                    }
                    try {
                        mask += str[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }
                isUpdating = true
                etTelefone.setText(mask)
                etTelefone.setSelection(mask.length)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
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

        if (fone.isEmpty() || fone.length < 13) {
            Toast.makeText(this, "Preencha o telefone corretamente!", Toast.LENGTH_SHORT).show()
            etTelefone.requestFocus()
            return
        }

        val contato = Contato(nome, fone, cat)
        if (idContato == -1) {
            dao.inserir(contato)
            Toast.makeText(this, "Contato salvo com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            contato.id = idContato
            dao.atualizar(contato)
            Toast.makeText(this, "Contato atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}