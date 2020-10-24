package com.example.kotlindemo3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DB_NAME = "FavoritesDB"
val TABLE_NAME = "Movies"
val COL_MOVIEID = "movieId"

class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_MOVIEID + " INTEGER PRIMARY KEY)";
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(movieId: Int) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_MOVIEID, movieId)
        var result = db.insert(TABLE_NAME, null, cv)
    }

    fun readData(): MutableList<Int> {
        var list: MutableList<Int> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                list.add(result.getInt(result.getColumnIndex(COL_MOVIEID)))
            } while (result.moveToNext())

            result?.close()
            db.close()
        }
        return list
    }

    fun deleteData(data: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_MOVIEID=?", arrayOf(data.toString()))
    }
}