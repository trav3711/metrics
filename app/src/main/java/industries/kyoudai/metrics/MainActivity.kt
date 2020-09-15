package industries.kyoudai.metrics

import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AnimatorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import industries.kyoudai.metrics.R.anim.*
import java.time.LocalDate


class MainActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener {

    private lateinit var recyclerView : RecyclerView
    private lateinit var add_button : FloatingActionButton
    private lateinit var metric_add_button : FloatingActionButton
    private lateinit var metric_update_button : FloatingActionButton
    private lateinit var metric_add_button_text : TextView
    private lateinit var metric_update_button_text : TextView


        lateinit var myDB : MyDatabase
    lateinit var metricItems : ArrayList<MetricItem>

    var isOpen = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        add_button = findViewById(R.id.add_button)
        metric_add_button = findViewById(R.id.metric_add_button)
        metric_update_button = findViewById(R.id.metric_update_button)
        metric_add_button_text = findViewById(R.id.metric_add_text)
        metric_update_button_text = findViewById(R.id.metric_update_text)

        /** animation imports */
        val fab_open = AnimationUtils.loadAnimation(this, fab_open)
        val fab_close = AnimationUtils.loadAnimation(this, fab_close)
        val fab_clock = AnimationUtils.loadAnimation(this, fab_rotate_clock)
        val fab_anticlock = AnimationUtils.loadAnimation(this, fab_rotate_anticlock)

        add_button.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            //startActivity(intent)

            if(isOpen){
                metric_add_button_text.visibility = View.INVISIBLE
                metric_update_button_text.visibility = View.INVISIBLE
                metric_add_button.startAnimation(fab_close)
                metric_update_button.startAnimation(fab_close)
                add_button.startAnimation(fab_anticlock)
                metric_add_button.isClickable = false
                metric_update_button.isClickable = false
                isOpen = false

            } else {
                metric_add_button_text.visibility = View.VISIBLE
                metric_update_button_text.visibility = View.VISIBLE
                metric_add_button.startAnimation(fab_open)
                metric_update_button.startAnimation(fab_open)
                add_button.startAnimation(fab_clock)
                metric_add_button.isClickable = true
                metric_update_button.isClickable = true
                isOpen = true;
            }
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
                var i = 0F
                while (cursor.moveToNext()) {
                    //var point: DataPoint = DataPoint(cursor.getDouble(2), cursor.getDouble(3))
                    var entry = BarEntry(i, cursor.getFloat(3))
                    i += 1
                    entries.add(entry)
                    Log.e(entry.toString(), "this entry")
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
