package industries.kyoudai.metrics

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.sql.Date

class TimestampAxisValueFormatter(private val chart: BarLineChartBase<*>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return super.getFormattedValue(value)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        //val date = SimpleDateFormat("mm:ss")
        //return date.format(Date.now())
        return super.getAxisLabel(value, axis)
    }

}