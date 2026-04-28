package com.edukaique.agenda.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.edukaique.agenda.db.DBHelper
import com.edukaique.agenda.model.Contato

class ContatoDAO(context: Context?) {
    private val dbHelper: DBHelper
    private var db: SQLiteDatabase? = null

    init {
        dbHelper = DBHelper(context)
    }

    fun inserir(contato: Contato): Long {
        db = dbHelper.getWritableDatabase()
        val values = ContentValues()
        values.put(DBHelper.COLUMN_NOME, contato.nome)
        values.put(DBHelper.COLUMN_TELEFONE, contato.telefone)
        values.put(DBHelper.COLUMN_CATEGORIA, contato.categoria)
        val id = db!!.insert(DBHelper.TABLE_CONTATOS, null, values)
        db!!.close()
        return id
    }

    fun atualizar(contato: Contato): Int {
        db = dbHelper.getWritableDatabase()
        val values = ContentValues()
        values.put(DBHelper.COLUMN_NOME, contato.nome)
        values.put(DBHelper.COLUMN_TELEFONE, contato.telefone)
        values.put(DBHelper.COLUMN_CATEGORIA, contato.categoria)
        val linhasAfetadas = db!!.update(
            DBHelper.TABLE_CONTATOS,
            values,
            DBHelper.COLUMN_ID + "=?",
            arrayOf<String>(contato.id.toString())
        )
        db!!.close()
        return linhasAfetadas
    }

    fun deletar(id: Int) {
        db = dbHelper.getWritableDatabase()
        db!!.delete(
            DBHelper.TABLE_CONTATOS,
            DBHelper.COLUMN_ID + "=?",
            arrayOf<String>(id.toString())
        )
        db!!.close()
    }

    private fun extrairContatoDoCursor(cursor: Cursor): Contato {
        val c = Contato()
        c.id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID))
        c.nome = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOME))
        c.telefone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TELEFONE))
        c.categoria = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CATEGORIA))
        return c
    }

    fun listarTodos(): MutableList<Contato?> {
        val lista: MutableList<Contato?> = ArrayList<Contato?>()
        db = dbHelper.getReadableDatabase()
        val cursor = db!!.query(
            DBHelper.TABLE_CONTATOS,
            null,
            null,
            null,
            null,
            null,
            DBHelper.COLUMN_NOME + " COLLATE NOCASE ASC"
        )
        while (cursor.moveToNext()) {
            lista.add(extrairContatoDoCursor(cursor))
        }
        cursor.close()
        db!!.close()
        return lista
    }

    fun buscarPorNome(termo: String?): MutableList<Contato?> {
        val lista: MutableList<Contato?> = ArrayList<Contato?>()
        db = dbHelper.getReadableDatabase()
        val cursor = db!!.query(
            DBHelper.TABLE_CONTATOS,
            null,
            DBHelper.COLUMN_NOME + " LIKE ?",
            arrayOf<String>("%" + termo + "%"),
            null,
            null,
            DBHelper.COLUMN_NOME + " ASC"
        )
        while (cursor.moveToNext()) {
            lista.add(extrairContatoDoCursor(cursor))
        }
        cursor.close()
        db!!.close()
        return lista
    }

    fun listarPorCategoria(categoria: String?): MutableList<Contato?> {
        val lista: MutableList<Contato?> = ArrayList<Contato?>()
        db = dbHelper.getReadableDatabase()
        val cursor = db!!.query(
            DBHelper.TABLE_CONTATOS,
            null,
            DBHelper.COLUMN_CATEGORIA + " = ?",
            arrayOf<String?>(categoria),
            null,
            null,
            DBHelper.COLUMN_NOME + " ASC"
        )
        while (cursor.moveToNext()) {
            lista.add(extrairContatoDoCursor(cursor))
        }
        cursor.close()
        db!!.close()
        return lista
    }

    val totalContatos: Int
        get() {
            db = dbHelper.getReadableDatabase()
            val cursor = db!!.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_CONTATOS, null)
            var count = 0
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
            cursor.close()
            db!!.close()
            return count
        }
}