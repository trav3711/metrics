package industries.kyoudai.metrics

import com.github.mikephil.charting.data.BarEntry
import java.io.*

/** data class holding the data for a card view in the recycler view */

// TODO: 8/28/20 make this data class also store the day/number data in the form of an array list

data class MetricItem(var metricID : Int,
                      var metricName : String,
                      @Transient var chartDataList : List<BarEntry>? = null): Serializable {
}