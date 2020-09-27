package industries.kyoudai.metrics

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.LargeValueFormatter
import org.w3c.dom.Text


private const val ARG_ID = "metric_id"
private const val ARG_NAME = "metric_name"
private const val ARG_DATA = "metric_data"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment(R.layout.activity_moreinfo) {
    lateinit private var myDB: MyDatabase
    //lateinit var titleName: String

    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var name: String? = null
    private var barDataList: ArrayList<BarEntry>? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString(ARG_ID)
        name = arguments?.getString(ARG_NAME)
        barDataList = arguments?.getParcelableArrayList<BarEntry>(ARG_DATA) as ArrayList<BarEntry>

        val titleView = getView()?.findViewById<TextView>(R.id.title)
        val recyclerView = getView()?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        val averageView : TextView? = getView()?.findViewById(R.id.average)

        titleView!!.text = name
        Graph(view, barDataList!!)

        averageView!!.text = "average: " + FindAverage(barDataList!!).toString()

        recyclerView!!.adapter = InfoRecylerAdapter(barDataList!!)

        activity?.let{
            myDB = MyDatabase(it)
            recyclerView!!.layoutManager = LinearLayoutManager(it)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun Graph(view: View, data: ArrayList<BarEntry>) = Unit.apply {
        val chart = getView()?.findViewById<BarChart>(R.id.barChart)
        val context = chart?.context

        var barDataSet = BarDataSet(data, "data list").also {
            it.color = context?.getColor(R.color.colorPrimary)!!
            it.valueTextSize = 12f
        };

        var barData = BarData(barDataSet).also {
            it.barWidth = 0.75f
            it.setDrawValues(false)
        }
        chart?.data = barData
        //chart.description.text = R.string.chart_description.toString()
        //chart.animateY(500)
        chart?.setVisibleXRangeMinimum(10f)
        chart?.setVisibleXRangeMaximum(100f)
        chart?.setNoDataText("No Data")

        // val xFormatter = DayAxisValueFormatter(chart)
        val xFormatter = chart?.let { DayAxisValueFormatter(it) }
        val LargeXFormatter = LargeValueFormatter()

        val xAxis = chart?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        // TODO: 9/14/20 figure out the value formatter
        xAxis?.valueFormatter = xFormatter
        //xAxis.setDrawAxisLine(false);
        xAxis?.setDrawGridLines(false);
        //xAxis.setDrawLabels(false)
        //var value = xAxis.valueFormatter.getAxisLabel(item.chartDataList[0].x, xAxis)
        //Log.i("Axis String", value)

        val yAxisLeft = chart!!.axisLeft
        //yAxisLeft.setDrawAxisLine(false)
        //yAxisLeft.setDrawGridLines(false)
        //yAxisLeft.setDrawLabels(false)
        yAxisLeft.setDrawZeroLine(true)
        //yAxisLeft.setStartAtZero(true)
        yAxisLeft.axisMinimum = 0f

        val yAxisRight = chart.axisRight
        //yAxisRight.setDrawAxisLine(false)
        //yAxisRight.setDrawGridLines(false)
        //yAxisRight.setDrawLabels(false)
        //yAxisRight.setDrawZeroLine(false)
        yAxisRight.axisMinimum = 0f

        val legend = chart?.legend
        legend!!.isEnabled = false

        val description = chart?.description
        description!!.isEnabled = false
    }

    fun FindAverage(list: ArrayList<BarEntry>) : Float{
        var a = 0f
        for(entry in list) {
            a += entry.y
        }
        return a/list.size
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(item: MetricItem) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, item.metricID.toString())
                    putString(ARG_NAME, item.metricName)
                    putParcelableArrayList(ARG_DATA, item.chartDataList)
                }
            }
    }
}