package industries.kyoudai.metrics

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import industries.kyoudai.metrics.MyDatabase as MyDatabase

/** adds an input to the SQLite database */

class AddActivity : AppCompatActivity() {

    lateinit var name_input : EditText
    lateinit var add_button : Button
    var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        name_input = findViewById(R.id.name_input)
        //unit_input = findViewById(R.id.unit_input)
        add_button = findViewById(R.id.second_add_button)

        add_button.setOnClickListener {
            val myDB = MyDatabase(this@AddActivity)
            myDB.addMetricItem(name_input.text.toString().trim())
            //startActivity(Intent(this, MainActivity::class.java))

            // Hide the keyboard.
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)


        }
    }

}