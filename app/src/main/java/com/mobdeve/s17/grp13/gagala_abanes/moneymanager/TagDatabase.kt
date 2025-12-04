package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TagDatabase(context: Context) :
    SQLiteOpenHelper(context, "tags.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE expense_tags(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                iconName TEXT NOT NULL
            );
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS expense_tags")
        onCreate(db)
    }

    fun insertExpenseTag(iconName: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("iconName", iconName)
        db.insert("expense_tags", null, cv)
    }

    fun deleteTag(iconName: String) {
        val db = writableDatabase
        db.delete("expense_tags", "iconName=?", arrayOf(iconName))
    }

    fun getAllExpenseTags(): List<String> {
        val db = readableDatabase
        val list = mutableListOf<String>()
        val c = db.rawQuery("SELECT iconName FROM expense_tags", null)
        while (c.moveToNext()) {
            list.add(c.getString(0))
        }
        c.close()
        return list
    }
}
