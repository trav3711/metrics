package industries.kyoudai.metrics

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class UpdateActivity : AppCompatActivity() {

    //private lateinit var metric_name : String
    //private var Metric_id : Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val metric_id = intent.getStringExtra("id")
        val metric_name = intent.getStringExtra("name")

        val quantity = findViewById<EditText>(R.id.editTextNumberSigned)
        val update_metric_name = findViewById<TextView>(R.id.nameText)
        var updateButton = findViewById<Button>(R.id.updateButton)
        var deleteButton: Button = findViewById(R.id.deleteButton)

        update_metric_name.text = metric_name

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
    }
}
