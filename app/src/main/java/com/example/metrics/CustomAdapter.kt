package com.example.metrics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jjoe64.graphview.GraphView
import kotlinx.android.synthetic.main.recycler_row.view.*

/** This adapter:
 *  - takes in view in the MyViewHolder subclass
 *  - uses onBindViewHolder to bind those views to the MetricItem data class
 */
class CustomAdapter(
    private val myList : ArrayList<MetricItem>,
    private val listener : OnItemClickListener
    ) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    /** Constructor */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CustomAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount() = myList.size

    override fun onBindViewHolder(holder: CustomAdapter.MyViewHolder, position: Int) {
        val currentItem = myList[position]

        holder.metric_id_txt.text = currentItem.metricID.toString()
        holder.metric_name_txt.text = currentItem.metricName
        holder.metric_unit_txt.text = currentItem.metricUnit
    }

    /** holds the views for which we want to give data to */
    inner class MyViewHolder(v : View) : RecyclerView.ViewHolder(v), OnClickListener{
        var metric_id_txt : TextView = v.findViewById(R.id.metric_id_text)
        var metric_name_txt : TextView = v.findViewById(R.id.metric_name_text)
        var metric_unit_txt : TextView = v.findViewById(R.id.metric_unit_text)
        var metric_graph : GraphView = v.findViewById(R.id.graph)

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemClick(myList[adapterPosition])
        }

    }

    /** Interface to handle a click on a metric item */
    interface OnItemClickListener {
        fun onItemClick(item : MetricItem)
    }

}