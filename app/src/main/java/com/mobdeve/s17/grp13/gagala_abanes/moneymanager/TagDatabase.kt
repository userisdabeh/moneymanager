package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TagDatabase(context: Context) :
    SQLiteOpenHelper(context, "tags.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        //create exp tags table
        db.execSQL("""
            CREATE TABLE expense_tags(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                iconName TEXT NOT NULL
            );
        """.trimIndent())

        //create inc tags table
        db.execSQL("""
            CREATE TABLE income_tags(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                iconName TEXT NOT NULL
            );
        """.trimIndent())

        // insert default tags for expense
        val defaultTags = listOf("education", "food", "leisure", "home", "transportation")
        for (tag in defaultTags) {
            val cv = ContentValues()
            cv.put("iconName", tag)
            db.insert("expense_tags", null, cv)
        }

        // insert default tags for income
        for (tag in defaultTags) {
            val cv = ContentValues()
            cv.put("iconName", tag)
            db.insert("income_tags", null, cv)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS expense_tags")
        db.execSQL("DROP TABLE IF EXISTS income_tags")
        onCreate(db)
    }

    //add tags
    fun insertExpenseTag(iconName: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("iconName", iconName)
        db.insert("expense_tags", null, cv)
    }

    fun insertIncomeTag(iconName: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("iconName", iconName)
        db.insert("income_tags", null, cv)
    }

    //delete tags
    fun deleteExpenseTag(iconName: String) {
        val db = writableDatabase
        db.delete("expense_tags", "iconName=?", arrayOf(iconName))
    }

    fun deleteIncomeTag(iconName: String) {
        val db = writableDatabase
        db.delete("income_tags", "iconName=?", arrayOf(iconName))
    }

    //get all tags
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

    fun getAllIncomeTags(): List<String> {
        val db = readableDatabase
        val list = mutableListOf<String>()
        val c = db.rawQuery("SELECT iconName FROM income_tags", null)
        while (c.moveToNext()) {
            list.add(c.getString(0))
        }
        c.close()
        return list
    }
}
