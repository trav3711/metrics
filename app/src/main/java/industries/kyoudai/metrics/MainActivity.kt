package industries.kyoudai.metrics

import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import industries.kyoudai.metrics.R.anim.*
import java.time.LocalDate

class MainActivity : AppCompatActivity(), MainRecylerAdapter.OnItemClickListener {

    private lateinit var recyclerView : RecyclerView
    private lateinit var add_button : FloatingActionButton
    private lateinit var metric_add_button : FloatingActionButton
    private lateinit var metric_update_button : FloatingActionButton
    private lateinit var metric_add_button_text : TextView
    private lateinit var metric_update_button_text : TextView

    lateinit var myDB : MyDatabase
    lateinit var metricItems : ArrayList<MetricItem>

    private lateinit var infoFragment : Fragment

    var isOpen = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** View imports */
        recyclerView = findViewById(R.id.recyclerView)
        add_button = findViewById(R.id.add_button)
        metric_add_button = findViewById(R.id.metric_add_button)
        metric_update_button = findViewById(R.id.metric_update_button)
        metric_add_button_text = findViewById(R.id.metric_add_text)
        metric_update_button_text = findViewById(R.id.metric_update_text)
        val shadow_view = findViewById<View>(R.id.shadowView)

        /** Fragment Imports*/
        infoFragment = BlankFragment()

        /** animation imports */
        val fab_open_fast = AnimationUtils.loadAnimation(this, fab_open_fast)
        val fab_open_slow = AnimationUtils.loadAnimation(this, fab_open_slow)
        val fab_close_fast = AnimationUtils.loadAnimation(this, fab_close_fast)
        val fab_close_slow = AnimationUtils.loadAnimation(this, fab_close_slow)
        val fab_clock = AnimationUtils.loadAnimation(this, fab_rotate_clock)
        val fab_anticlock = AnimationUtils.loadAnimation(this, fab_rotate_anticlock)
        val text_show = AnimationUtils.loadAnimation(this, text_show)
        val text_show_slow = AnimationUtils.loadAnimation(this, text_show_slow)
        val text_close = AnimationUtils.loadAnimation(this, text_close)
        val text_close_slow = AnimationUtils.loadAnimation(this, text_close_slow)

        myDB = MyDatabase(this)
        metricItems = ArrayList()

        createMetricItem()

        recyclerView.adapter = MainRecylerAdapter(metricItems, this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        /** click listeners */
        add_button.setOnClickListener{
            if(isOpen){
                metric_add_button_text.startAnimation(text_close)
                metric_update_button_text.startAnimation(text_close_slow)
                shadow_view.startAnimation(text_close_slow)
                metric_add_button_text.visibility = View.INVISIBLE
                metric_update_button_text.visibility = View.INVISIBLE
                shadow_view.visibility = View.INVISIBLE
                metric_add_button.startAnimation(fab_close_fast)
                metric_update_button.startAnimation(fab_close_slow)
                add_button.startAnimation(fab_anticlock)
                metric_add_button.isClickable = false
                metric_update_button.isClickable = false
                isOpen = false

            } else {
                metric_add_button_text.startAnimation(text_show_slow)
                metric_update_button_text.startAnimation(text_show)
                shadow_view.startAnimation(text_show_slow)
                metric_add_button_text.visibility = View.VISIBLE
                metric_update_button_text.visibility = View.VISIBLE
                shadow_view.visibility = View.VISIBLE
                metric_add_button.startAnimation(fab_open_slow)
                metric_update_button.startAnimation(fab_open_fast)
                add_button.startAnimation(fab_clock)
                metric_add_button.isClickable = true
                metric_update_button.isClickable = true
                isOpen = true;
            }
        }

        metric_add_button.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        metric_update_button.setOnClickListener {
            val intent = Intent(this@MainActivity, UpdateActivity::class.java)
            intent.putExtra("metricItemsList", metricItems)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMetricItem() {
        var cursor : Cursor? = myDB.readAllMainData()
        if (cursor != null) {
            if(cursor.count == 0){
                Toast.makeText(this, "No Database Available", Toast.LENGTH_SHORT).show()
            } else {
                var entries : ArrayList<BarEntry?> = ArrayList()
                while (cursor.moveToNext()) {
                    var item = MetricItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        entries
                    )
                    item.chartDataList = createGraph(item)
                    metricItems.add(item)
                    Log.e("item exist", item.metricName)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createGraph(item: MetricItem) : ArrayList<BarEntry?> {
        var entries: ArrayList<BarEntry?> = ArrayList()
        val cursor: Cursor? = myDB.readAllSecondaryData(item.metricID)

        if (cursor != null) {
            if (cursor.count != 0) {
                var i = 0F
                while (cursor.moveToNext()) {
                    var entry = BarEntry(i, cursor.getFloat(4))
                    i += 1
                    entries.add(entry)
                }
            }
        }
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
        /*val intent = Intent(this, MoreInfoActivity::class.java)
        intent.putExtra("id", item.metricID.toString())
        intent.putExtra("name", item.metricName)
        startActivity(intent)*/
        infoFragment = BlankFragment.newInstance(item)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.info_fragment, infoFragment)
            addToBackStack(infoFragment.toString())

            //setResult(123, item.metricID)
            commit()
        }
    }
}

