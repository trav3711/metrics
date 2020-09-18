package industries.kyoudai.metrics

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class UpdateActivity : AppCompatActivity() {

    //private lateinit var metric_name : String
    //private var Metric_id : Int = 0
    lateinit var myDB : MyDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        myDB = MyDatabase(this)

        val metric_id = intent.getStringExtra("id")
        val metric_name = intent.getStringExtra("name")

        val quantity = findViewById<EditText>(R.id.editTextNumberSigned)
        //val update_metric_name = findViewById<TextView>(R.id.nameText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val deleteButton: Button = findViewById(R.id.deleteButton)
        val metrics_spinner = findViewById<Spinner>(R.id.metricSpinner)

        //update_metric_name.text = metric_name

        updateButton.setOnClickListener {
            //Toast.makeText(this, metric_name, Toast.LENGTH_SHORT).show()
            val myDB = MyDatabase(this@UpdateActivity)
            if (metric_id != null) {
                //Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
                myDB.updateMetricItem(metric_id.toInt(), quantity.text)
            }

            // Hide the keyboard.
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            startActivity(Intent(this, MainActivity::class.java))
        }

        deleteButton.setOnClickListener {
            val myDB = MyDatabase(this@UpdateActivity)
            if(metric_id != null) {
                Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show()
                myDB.removeMetricItem(metric_id)
            }
            startActivity(Intent(this, MainActivity::class.java))
        }

        //val names_list = createMetricNamesList()
        metrics_spinner.adapter = CustomArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
            intent.extras?.get("metricsItemsList") as ArrayList<MetricItem>
        )
        metrics_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@UpdateActivity, adapterView?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun createMetricNamesList() : ArrayList<String> {
        var res : ArrayList<String> = ArrayList()
        val cursor = myDB.readMetricNamesByTime()
        if (cursor != null) {
            if (cursor.count == 0) {
                Toast.makeText(this, "No Names Available", Toast.LENGTH_SHORT).show()
            } else {
                while (cursor.moveToNext()){
                    res.add(cursor.getString(0))
                }
            }
        }
        return res
    }
}
