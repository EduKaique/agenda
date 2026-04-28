package com.edukaique.agenda.model;

data class HPChar(
    val id: Int = 0,
    val name: String,
    val house: String,
    val ancestry: String
) {
    override fun toString(): String {
        return name
    }
}