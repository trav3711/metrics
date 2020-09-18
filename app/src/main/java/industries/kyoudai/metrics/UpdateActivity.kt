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
    lateinit var myDB : MyDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        myDB = MyDatabase(this)

        var metric_id = ""

        val quantity = findViewById<EditText>(R.id.editTextNumberSigned)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val metrics_spinner = findViewById<Spinner>(R.id.metricSpinner)

        updateButton.setOnClickListener {
            if (metric_id != null) {
                myDB.updateMetricItem(metric_id.toInt(), quantity.text)
            }

            // Hide the keyboard.
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            startActivity(Intent(this, MainActivity::class.java))
        }

        val key_name_map = createMetricNamesHashMap()
        val names_list = ArrayList<String>()
        for(key in key_name_map.keys){
            key_name_map[key]?.let { names_list.add(it) }
        }
        metrics_spinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, names_list)
        /*metrics_spinner.adapter = CustomArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
            intent.extras?.get("metricsItemsList") as ArrayList<MetricItem>
        )*/
        metrics_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                metric_id = getKey(key_name_map, adapterView?.getItemAtPosition(position).toString()).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun createMetricNamesHashMap() : HashMap<Int, String> {
        val hashMap:HashMap<Int,String> = HashMap() //define empty hashmap
        val cursor = myDB.readMetricNamesByTime()
        if (cursor != null) {
            if (cursor.count == 0) {
                Toast.makeText(this, "No Names Available", Toast.LENGTH_SHORT).show()
            } else {
                while (cursor.moveToNext()){
                    hashMap.put(cursor.getInt(0), cursor.getString(1))
                }
            }
        }
        return hashMap
    }

    fun <K, V> getKey(hashMap: Map<K, V>, target: V): K {
        return hashMap.filter { target == it.value }.keys.first()
    }
}
