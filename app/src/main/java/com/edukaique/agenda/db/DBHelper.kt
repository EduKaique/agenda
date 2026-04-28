package com.edukaique.agenda.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + TABLE_CONTATOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT NOT NULL, " +
                COLUMN_TELEFONE + " TEXT NOT NULL, " +
                COLUMN_CATEGORIA + " TEXT NOT NULL)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTATOS)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "agenda.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_CONTATOS: String = "contatos"
        const val COLUMN_ID: String = "id"
        const val COLUMN_NOME: String = "nome"
        const val COLUMN_TELEFONE: String = "telefone"
        const val COLUMN_CATEGORIA: String = "categoria"
    }
}