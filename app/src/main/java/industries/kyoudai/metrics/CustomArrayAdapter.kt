package industries.kyoudai.metrics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes

class CustomArrayAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    val metricItems: ArrayList<MetricItem>
) : ArrayAdapter<MetricItem>(context, layoutResource, metricItems ) {

    override fun getItem(position: Int): MetricItem? = metricItems[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = getItem(position)?.metricName
        return view
    }
}