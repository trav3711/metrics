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
import com.jjoe64.graphview.GraphView
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

    // TODO: 8/29/20 add graph funcationality which I think goes here
    fun createMetricItem() {
        var cursor : Cursor? = myDB.readAllMainData()
        if (cursor != null) {
            if(cursor.count == 0){
                Toast.makeText(this, "No Database Available", Toast.LENGTH_SHORT).show()
            } else {
                var matrix : MutableList<MutableList<Int>> = mutableListOf()
                while (cursor.moveToNext()) {
                    var item = MetricItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        matrix)
                    // TODO: 8/29/20 right here is where I implement createGraph
                    item.graphMatrix = createGraph(item)


                    metricItems.add(item)
                }
            }
        }
    }

    // TODO: 8/29/20 but I want to handle all the graph stuff in this function
    // TODO: 8/29/20 handle all of the graph stuff means put it into a data structure that can be stored by the MetricItem object
    fun createGraph(item : MetricItem) : MutableList<MutableList<Int>>{
        //var dates : ArrayList<String>
        var graphMatrix : MutableList<MutableList<Int>> = mutableListOf()
        var currentDate : String?

        var graph = findViewById<GraphView>(R.id.graph)
        var dateCursor : Cursor? = myDB.readAllSecondaryData(item.metricID)
        var quantityCursor : Cursor? = dateCursor

        if(dateCursor != null){
            if(dateCursor.count == 0){
                Toast.makeText(this, "no secondary database available", Toast.LENGTH_SHORT).show()
            } else {
                var myList: MutableList<Int>
                var initPosition : Int = dateCursor.position
                var finalposition: Int
                currentDate = dateCursor.getString(2)

                while(dateCursor.moveToNext()){
                    if(dateCursor.getString(2) != currentDate){

                        finalposition = dateCursor.position
                        quantityCursor?.moveToPosition(initPosition)
                        myList = mutableListOf()

                        for (i in initPosition until finalposition) {
                            if (quantityCursor != null) {
                                myList.add(quantityCursor.getInt(3))
                                quantityCursor.moveToNext()
                            }

                        }
                        //this needs to be fixed
                        graphMatrix.add(myList)
                        currentDate = dateCursor.getString(2)
                        initPosition = finalposition
                    } else {
                        continue
                    }

                }
            }
        }
        return graphMatrix
    }

    @ExperimentalStdlibApi
    override fun onItemClick(item : MetricItem) {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("id", item.getID().toString())
        intent.putExtra("name", item.metricName)
        intent.putExtra("unit", item.metricUnit)
        startActivity(intent)
    }
}
