package industries.kyoudai.metrics

import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import java.time.LocalDate


class MainActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener {

    private lateinit var recyclerView : RecyclerView
    private lateinit var add_button : FloatingActionButton
    lateinit var myDB : MyDatabase
    lateinit var metricItems : ArrayList<MetricItem>

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMetricItem() {
        var cursor : Cursor? = myDB.readAllMainData()
        if (cursor != null) {
            if(cursor.count == 0){
                Toast.makeText(this, "No Database Available", Toast.LENGTH_SHORT).show()
            } else {
                var entries : ArrayList<BarEntry> = ArrayList()
                while (cursor.moveToNext()) {
                    var item = MetricItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        entries
                    )
                    item.chartDataList = createGraph(item)
                    metricItems.add(item)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createGraph(item: MetricItem) : ArrayList<BarEntry> {
        var entries: ArrayList<BarEntry> = ArrayList()
        var graphData: BarGraphSeries<DataPoint> = BarGraphSeries()
        val cursor: Cursor? = myDB.readAllSecondaryData(item.metricID)

        if (cursor != null) {
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    var point: DataPoint = DataPoint(cursor.getDouble(2), cursor.getDouble(3))
                    entries.add(BarEntry(cursor.getFloat(2), cursor.getFloat(3)))
                }
            }
        }
        //generateDummyData(entries)
        return entries
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateDummyData(entries: ArrayList<BarEntry>) {
        val now = LocalDate.now().toEpochDay()
        for(i in 1 until 200) {
            var num : Int = (0..100).random()
            entries.add(BarEntry((now + i).toFloat(), num.toFloat()))
        }

    }

    @ExperimentalStdlibApi
    override fun onItemClick(item: MetricItem) {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("id", item.getID().toString())
        intent.putExtra("name", item.metricName)
        startActivity(intent)
    }
}
