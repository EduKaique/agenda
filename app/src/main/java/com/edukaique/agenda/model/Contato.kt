package com.edukaique.agenda.model

class Contato {
    var id: Int = 0
    var nome: String? = null
    var telefone: String? = null
    var categoria: String? = null

    constructor()

    constructor(nome: String?, telefone: String?, categoria: String?) {
        this.nome = nome
        this.telefone = telefone
        this.categoria = categoria
    }

    override fun toString(): String {
        return nome + " (" + categoria + ")\n" + telefone
    }
}