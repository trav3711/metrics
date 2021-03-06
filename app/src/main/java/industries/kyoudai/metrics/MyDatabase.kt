package industries.kyoudai.metrics

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Editable
import android.widget.Toast

class MyDatabase(private val context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val query1 = "CREATE TABLE " + MAIN_TABLE_NAME + " (" +
                MAIN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MAIN_COLUMN_NAME + " TEXT" + ");"

        val query2 = "CREATE TABLE " + SECOND_TABLE_NAME + " (" +
                SECOND_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SECOND_COLUMN_METRIC_ID + " INTEGER," +
                SECOND_COLUMN_SECONDS + " DATETIME DEFAULT (strftime('%s','now'))," +
                SECOND_COLUMN_TIME + " DATETIME DEFAULT TIMESTAMP," +
                SECOND_COLUMN_QUANTITY + " INTEGER" + ");"

        db.execSQL(query1)
        db.execSQL(query2)

    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS $MAIN_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $SECOND_TABLE_NAME")
        onCreate(db)
    }

    fun addMetricItem(name: String?) : Int {
        val dbWrite = this.writableDatabase
        val cv = ContentValues()
        cv.put(MAIN_COLUMN_NAME, name.toString())

        val result = dbWrite.insert(MAIN_TABLE_NAME, null, cv).toInt()
        if (result == -1) {
            Toast.makeText(context, "main table ailed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "main success!", Toast.LENGTH_SHORT).show()
        }
        return result
    }
    
    fun updateMetricItem(metric_id: Int?, quantity: Editable) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(SECOND_COLUMN_METRIC_ID, metric_id)
        //cv.put(SECOND_COLUMN_TIME, date)
        cv.put(SECOND_COLUMN_QUANTITY, quantity.toString())

        val result = db.insert(SECOND_TABLE_NAME, null, cv)
        if (result == -1L){
            Toast.makeText(context, "secondary table failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "secondary success!", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeMetricItem(metric_id: String?) {
        val db = this.writableDatabase
        db.delete(MAIN_TABLE_NAME, "$MAIN_COLUMN_ID IS $metric_id", null)
        db.delete(SECOND_TABLE_NAME, "$SECOND_COLUMN_METRIC_ID IS $metric_id", null)
    }

    fun readAllMainData(): Cursor? {
        val query = "SELECT * FROM $MAIN_TABLE_NAME"
        val db = this.readableDatabase
        var cursor : Cursor? = null
        if(db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun readAllSecondaryData(id : Int) : Cursor? {
        val query = "SELECT * FROM $SECOND_TABLE_NAME WHERE $SECOND_COLUMN_METRIC_ID IS $id"
        val db = this.readableDatabase
        var cursor : Cursor? = null
        if(db != null) {
            cursor = db.rawQuery(query, null)
            //Log.i("is cursor null?", cursor.toString())
        }
        return cursor
    }

    fun readMetricNamesByTime() : Cursor {
        //val query =
        //    "SELECT DISTINCT $MAIN_COLUMN_NAME FROM $MAIN_TABLE_NAME " +
        //        "INNER JOIN $SECOND_TABLE_NAME ON $SECOND_TABLE_NAME.$SECOND_COLUMN_METRIC_ID = $MAIN_TABLE_NAME.$MAIN_COLUMN_ID " +
        //        "GROUP BY $SECOND_TABLE_NAME.$SECOND_COLUMN_SECONDS " +
        //        "ORDER BY $SECOND_TABLE_NAME.$SECOND_COLUMN_SECONDS DESC"
        val query = "SELECT $MAIN_COLUMN_ID, $MAIN_COLUMN_NAME FROM $MAIN_TABLE_NAME"
        val db = this.readableDatabase
        val  cursor = db.rawQuery(query, null)
        return cursor
    }

    companion object {
        private const val DATABASE_NAME = "MetricLibrary.db"
        private const val DATABASE_VERSION = 1

        private const val MAIN_TABLE_NAME = "my_metric_main_table"
        private const val MAIN_COLUMN_ID = "id"
        private const val MAIN_COLUMN_NAME = "metric_name"

        private const val SECOND_TABLE_NAME = "my_metrics_secondary_table"
        private const val SECOND_COLUMN_ID = "id"
        private const val SECOND_COLUMN_METRIC_ID = "metric_id"
        private const val SECOND_COLUMN_SECONDS = "epoch_time_seconds"
        private const val SECOND_COLUMN_TIME = "date_seconds"
        private const val SECOND_COLUMN_QUANTITY = "number"
    }

}
