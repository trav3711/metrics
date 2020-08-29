package com.example.metrics

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.recycler_row.*
import java.util.zip.Inflater
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener {

    private lateinit var recyclerView : RecyclerView
    private lateinit var add_button : FloatingActionButton
    lateinit var myDB : MyDatabase
    lateinit var metricItems : ArrayList<MetricItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        add_button = findViewById(R.id.add_button)
        add_button.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        myDB = MyDatabase(this)
        metricItems = ArrayList()

        createMetricItem()

        recyclerView.adapter = CustomAdapter(metricItems, this)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    /**override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            recreate()
        }
    }*/

    fun createMetricItem() {
        var cursor : Cursor? = myDB.readAllData()
        if (cursor != null) {
            if(cursor.count == 0){
                Toast.makeText(this, "No Database Available", Toast.LENGTH_SHORT).show()
            } else {
                while (cursor.moveToNext()) {
                    var item = MetricItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2))

                    //Toast.makeText(this, item.metricID, Toast.LENGTH_LONG).show()
                    metricItems.add(item)
                }
            }
        }
    }

    fun updateMetricItem() {

    }

    @ExperimentalStdlibApi
    override fun onItemClick(item : MetricItem) {
        //Toast.makeText(this, item.getID().toString(), Toast.LENGTH_SHORT).show()

        // TODO: 8/28/20 navigate to an update activity and update the metric item
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("id", item.getID().toString())
        intent.putExtra("name", item.metricName)
        intent.putExtra("unit", item.metricUnit)
        startActivity(intent)
    }
}
