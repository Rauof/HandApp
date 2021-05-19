package com.rauof.hand

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MyCustomCursorAdapter(context: Context, cursor: Cursor?):CursorAdapter(context, cursor, 0) {

    private class ViewHolder {
        var name: TextView? = null
        var score:TextView? = null
        var history:Button? = null
        var total_games:TextView? = null
        var last_game:TextView? = null
        var historyText:String? = null
        var numOfTimesPlayed:TextView? = null

    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val layoutInflater =
            LayoutInflater.from(context)
        val newView = layoutInflater
            .inflate(
                R.layout.item_list_custom, parent, false
            )
        val viewHolder = ViewHolder()
        viewHolder.name =
            newView.findViewById(R.id.name)
        viewHolder.score =
            newView.findViewById(R.id.score)
        viewHolder.history =
            newView.findViewById(R.id.history)
        viewHolder.last_game =
            newView.findViewById(R.id.last_game)
        viewHolder.total_games =
            newView.findViewById(R.id.total_games)
//        viewHolder.histor_text =
//            newView.findViewById(R.id.txt_hist)
        //viewHolder.auto_incr = newView.findViewById(R.id.auto_increament)
        // store the viewHolder object with the new view using the tag
        newView.tag = viewHolder
        return newView
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        // get the viewHolder from the newView object via the Tag object
        val viewHolder = view!!.tag as ViewHolder
        viewHolder.name?.text = cursor?.getString(cursor.getColumnIndex(MySqlHelper.NAME))
        viewHolder.total_games?.text = cursor?.getString(cursor.getColumnIndex(MySqlHelper.TOTLA_GAMES))
        viewHolder.last_game?.text = cursor?.getString(cursor.getColumnIndex(MySqlHelper.LAST_GAME))
        viewHolder.historyText = cursor?.getString(cursor.getColumnIndex(MySqlHelper.HISTORY))
        viewHolder.numOfTimesPlayed?.text = cursor?.getInt(cursor.getColumnIndex(MySqlHelper.Num_OF_TIMES_PLAYED)).toString()
        viewHolder.history?.setOnClickListener{
            val edT = viewHolder.historyText
            Toast.makeText(context, edT, Toast.LENGTH_SHORT).show()
        }
    }

    fun getScoreFromEditText(view: View?):String{
        val viewHolder = view!!.tag as ViewHolder
        val edT = viewHolder.score?.text.toString()
        return edT
    }

    fun clearScoresFromEditText(view: View?){
        val viewHolder = view!!.tag as ViewHolder
        viewHolder.score?.text = ""
    }

    fun getTotalGames(view: View?):String{
        val viewHolder = view!!.tag as ViewHolder
        return viewHolder.total_games?.text.toString()
    }

/*
fun changeBackgrounds(view: View, view1: View) {
view.setBackgroundColor(R.color.red)
view1.setBackgroundColor(R.color.green)
}
fun changeBackgrounds(view: View, view1: View) {

}
*/

}