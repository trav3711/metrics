package com.example.metrics

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabase(private val context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," + COLUMN_UNIT + " TEXT" + ");"
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addMetric(name: String?, units: String?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_NAME, name.toString())
        cv.put(COLUMN_UNIT, units.toString())
        val result = db.insert(TABLE_NAME, null, cv)
        if (result == -1L) {
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "success!", Toast.LENGTH_SHORT).show()
        }
    }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor : Cursor? = null
        if(db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    companion object {
        private const val DATABASE_NAME = "MetricLibrary.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "my_metrics"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "metric_name"
        private const val COLUMN_UNIT = "metric_unit"
    }

}