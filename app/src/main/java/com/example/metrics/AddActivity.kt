package com.example.metrics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.metrics.MyDatabase as MyDatabase

/** adds an input to the SQLite database */

class AddActivity : AppCompatActivity() {

    lateinit var name_input : EditText
    lateinit var unit_input : EditText
    lateinit var add_button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        name_input = findViewById(R.id.name_input)
        unit_input = findViewById(R.id.unit_input)
        add_button = findViewById<Button>(R.id.second_add_button)

        add_button.setOnClickListener {
            val myDB = MyDatabase(this@AddActivity)
            print(name_input.toString().trim())
            myDB.addMetric(name_input.text.toString().trim(), unit_input.text.toString().trim())


        }
    }
}