package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TransactionDatabase (context: Context) : SQLiteOpenHelper(context, "transactions.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
                CREATE TABLE transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    currency TEXT NOT NULL,
                    date TEXT NOT NULL,
                    tag TEXT NOT NULL,
                    comments TEXT,
                    photos TEXT
                );
                """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS transactions")
        onCreate(db)
    }

    fun insertTransaction(
        type: String,
        amount: Double,
        currency: String,
        tag: String,
        date: String,
        comments: String,
        photoUris: List<String>
    ) : Long {
        val db = writableDatabase
        val cv = ContentValues().apply{
            put("type", type)
            put("amount", amount)
            put("currency", currency)
            put("tag", tag)
            put("date", date)
            put("comments", comments)
            put("photos", photoUris.joinToString("|"))
        }
        val result = db.insert("transactions", null, cv)
        db.close()
        return result
    }

    fun getAllTransactions(): List<Transaction> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM transactions ORDER BY id DESC", null)
        val list = mutableListOf<Transaction>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"))
                val currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"))
                val tag = cursor.getString(cursor.getColumnIndexOrThrow("tag"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"))
                val photos = cursor.getString(cursor.getColumnIndexOrThrow("photos"))

                list.add(Transaction(id, type, amount, currency, tag, date, comments, photos))
            } while (cursor.moveToNext())
        }

        Log.d("MoneyManagerDB", "Transactions loaded: ${list.size}")

        cursor.close()
        db.close()
        return list
    }
}