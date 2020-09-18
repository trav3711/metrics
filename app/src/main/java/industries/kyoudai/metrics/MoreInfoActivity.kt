package industries.kyoudai.metrics

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MoreInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moreinfo)

        val deleteButton: Button = findViewById(R.id.deleteButton)
        val metric_id = intent.getStringExtra("id")

        deleteButton.setOnClickListener {
            val myDB = MyDatabase(this@MoreInfoActivity)
            if(metric_id != null) {
                Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show()
                myDB.removeMetricItem(metric_id)
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}