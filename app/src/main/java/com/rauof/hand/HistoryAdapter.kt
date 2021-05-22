package com.rauof.hand

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class HistoryAdapter(context: Context, cursor: Cursor?):CursorAdapter(context, cursor, 0) {

    private class ViewHolder {
        var name: TextView? = null
        var info:TextView? = null
    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val layoutInflater =
                LayoutInflater.from(context)
        val newView = layoutInflater
                .inflate(
                        R.layout.history_list, parent, false
                )
        val viewHolder = ViewHolder()
        viewHolder.name =
                newView.findViewById(R.id.h_name)
        viewHolder.info=
                newView.findViewById(R.id.h_info)
        newView.tag = viewHolder
        return newView
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        // get the viewHolder from the newView object via the Tag object
        val viewHolder = view!!.tag as ViewHolder
        viewHolder.name?.text = cursor?.getString(cursor.getColumnIndex(MySqlHelper.NAME))
        viewHolder.info?.text = cursor?.getString(cursor.getColumnIndex(MySqlHelper.HISTORY))
    }
}