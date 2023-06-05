package com.morphylix.android.petkeeper.util

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T> handleHintData(
    data: List<T>,
    context: Context,
    editText: AutoCompleteTextView,
    mapData: (data: List<T>) -> List<Any?>
): MutableList<T> {
    val mappedData = mapData.invoke(data)
    val adapter = ArrayAdapter(
        context,
        R.layout.simple_list_item_1,
        mappedData.toTypedArray()
    )
    editText.setAdapter(adapter)
    adapter.notifyDataSetChanged()
    return data.toMutableList()
}

inline fun <reified T> provideOnItemClickListener(
    lastFetchedData: List<Any>,
    chosenItemName: String,
    chosenItemIndex: Int,
    mapData: (data: List<Any>) -> List<String>,
    doAfterItemWasFound: (item: T) -> Unit
) {
    val mappedData = mapData.invoke(lastFetchedData)
    if (lastFetchedData.firstOrNull() is T) { // tbh it's always true but whatever
        mappedData.forEach { itemName ->
            if (itemName == chosenItemName) {
                doAfterItemWasFound.invoke(lastFetchedData[chosenItemIndex] as T)
            }
        }
    }
}

fun formatDate(date: Date): String {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return parser.format(date)
}

fun test() {

}