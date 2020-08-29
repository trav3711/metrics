package com.example.metrics

import androidx.annotation.Nullable

/** data class holding the data for a card view in the recycler view */

// TODO: 8/28/20 make this data class also store the day/number data in the form of an array list
data class MetricItem(var metricID : Int,
                      var metricName : String,
                      var metricUnit : String,
                      var graphMatrix : MutableList<MutableList<Int>>) {

    fun getID() : Int {
        return this.metricID
    }
}