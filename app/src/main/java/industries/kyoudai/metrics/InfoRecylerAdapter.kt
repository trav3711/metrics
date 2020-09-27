package industries.kyoudai.metrics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry

class InfoRecylerAdapter(
    private val dataList: ArrayList<BarEntry>
) : RecyclerView.Adapter<InfoRecylerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InfoRecylerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.info_recycler_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: InfoRecylerAdapter.MyViewHolder, position: Int) {
        val currentEntry = dataList[position]
        holder.timestamp_text.text = currentEntry.x.toInt().toString()
        holder.entry_text.text = currentEntry.y.toInt().toString()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v){
        var timestamp_text : TextView = v.findViewById(R.id.timestamp_text)
        var entry_text : TextView = v.findViewById(R.id.entry_quantity_text)
    }
}