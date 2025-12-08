package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TagDatabase(context: Context) :
    SQLiteOpenHelper(context, "tags.db", null, 10) {

    override fun onCreate(db: SQLiteDatabase) {
        //create exp tags table
        db.execSQL("""
            CREATE TABLE expense_tags(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                iconName TEXT NOT NULL UNIQUE,
                displayName TEXT NOT NULL
            );
        """.trimIndent())

        //create inc tags table
        db.execSQL("""
            CREATE TABLE income_tags(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                iconName TEXT NOT NULL UNIQUE,
                displayName TEXT NOT NULL
            );
        """.trimIndent())

        // insert default tags for expense
        val defaultTags = listOf(
            "def_bluedu" to "Food",
            "def_redfood" to "Education",
            "def_grnles" to "Leisure",
            "def_ylwhome" to "Home",
            "def_orgtranspo" to "Transportation")

        for ((iconName, displayName) in defaultTags) {
            val cv = ContentValues().apply{
                put("iconName", iconName)
                put("displayName", displayName)
            }

            db.insert("expense_tags", null, cv)
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
        val cv = ContentValues().apply{
            put("iconName", iconName)
            put("displayName", iconName)
        }
        db.insert("expense_tags", null, cv)
    }

    fun insertIncomeTag(iconName: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("iconName", iconName)
            put("displayName", iconName)
        }
        db.insert("income_tags", null, cv)
    }

    //delete tags
    //delete tags
    fun deleteExpenseTag(iconName: String) {
        val defaultTags = listOf("def_bluedu", "def_redfood", "def_grnles", "def_ylwhome", "def_orgtranspo")
        if (iconName in defaultTags) return  // prevent deletion of default tags
        val db = writableDatabase
        db.delete("expense_tags", "iconName=?", arrayOf(iconName))
    }

    fun deleteIncomeTag(iconName: String) {
        val defaultTags = listOf("def_bluedu", "def_redfood", "def_grnles", "def_ylwhome", "def_orgtranspo")
        if (iconName in defaultTags) return  // prevent deletion of default tags
        val db = writableDatabase
        db.delete("income_tags", "iconName=?", arrayOf(iconName))
    }


    //get all tags
    fun getAllExpenseTags(): List<TagItem> {
        val db = readableDatabase
        val list = mutableListOf<TagItem>()
        val c = db.rawQuery("SELECT iconName, displayName FROM expense_tags", null)
        while (c.moveToNext()) {
            val iconName = c.getString(0)
            val displayName = c.getString(1)
            list.add(TagItem(iconName, displayName))
        }
        c.close()
        return list
    }

    fun getAllIncomeTags(): List<TagItem> {
        val db = readableDatabase
        val list = mutableListOf<TagItem>()
        val c = db.rawQuery("SELECT iconName, displayName FROM income_tags", null)
        while (c.moveToNext()) {
            val iconName = c.getString(0)
            val displayName = c.getString(1)
            list.add(TagItem(iconName, displayName))
        }
        c.close()
        return list
    }
}
