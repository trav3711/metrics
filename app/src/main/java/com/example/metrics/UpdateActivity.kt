package com.example.metrics

import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class UpdateActivity : AppCompatActivity() {

    // TODO: 8/28/20 first text view should display metric name(having trouble)
    // TODO: 8/28/20 second text view should display metric unit(Having trouble)
    // TODO: 8/28/20 date entry should show default today's date
    // TODO: 8/28/20 add the whole adding metric activity functionality

    //private lateinit var metric_name : String
    //private var Metric_id : Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        //var activity = MainActivity().findViewById<TextView>(R.id.metric_id_text)

        val metric_id = intent.getStringExtra("id")
        val metric_name = intent.getStringExtra("name")
        val metric_unit = intent.getStringExtra("unit")

        val date = findViewById<EditText>(R.id.editTextDate)
        val quantity = findViewById<EditText>(R.id.editTextNumberSigned)
        val update_metric_name = findViewById<TextView>(R.id.nameText)
        val update_metric_unit = findViewById<TextView>(R.id.unitText)
        var updateButton = findViewById<Button>(R.id.updateButton)

        update_metric_name.text = metric_name
        update_metric_unit.text = metric_unit

        var date_text : String = LocalDate.now().toString().trim()
        date.setText(date_text)

        updateButton.setOnClickListener {
            //Toast.makeText(this, metric_name, Toast.LENGTH_SHORT).show()
            val myDB = MyDatabase(this@UpdateActivity)
            if (metric_id != null) {
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
                myDB.updateMetricItem(metric_id.toInt(), date.text, quantity.text)
            }

            // Hide the keyboard.
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        //val localDate : LocalDate = LocalDate.now()
        //val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("dd LLLL yyyy")



    }
}
