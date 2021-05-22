package com.rauof.hand

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.size
import com.rauof.hand.databinding.ActivityListBinding
import java.text.SimpleDateFormat

class ListActivity : AppCompatActivity() {

    private lateinit var myCustomCursorAdapter:MyCustomCursorAdapter
    private lateinit var myHelper: MySqlHelper
    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myHelper = MySqlHelper(applicationContext)

        customCusorAdapter()

        findViewById<Button>(R.id.addplayer).setOnClickListener {
            if(binding.newPlayerEditText.text.isNotEmpty()){
                saveDataToDatabase()
                clearText()
            }
            else
                showToast(getString(R.string.empty_player_name))
        }

        findViewById<Button>(R.id.calc).setOnClickListener {
            var arrHoldingScores = IntArray(binding.content.listview.size)
            var isAnyScoreNull = false
            //test if all scores are entered
            for (i in 0 until binding.content.listview.size){
                if(myCustomCursorAdapter.getScoreFromEditText(binding.content.listview[i]).isNotEmpty())
                    arrHoldingScores[i] = myCustomCursorAdapter.getScoreFromEditText(binding.content.listview[i]).toInt()
                else
                    isAnyScoreNull = true;
            }
            if(!isAnyScoreNull){    // if all players' scores are inserted
                //get the total games' scores from the data base
                var lastTotalGameScores = myHelper.getTotalGameScores(binding.content.listview.size)
                var numOfTimesPLayed = myHelper.getNumOfTimesPlayed(binding.content.listview.size)
                var names = myHelper.getNames()
                var histories = myHelper.getHistory()
                //update all players' scores, last games and total scores
                for (i in 0 until binding.content.listview.size) {
                    //update data for the player with index i
                    myHelper.updateLastGame(arrHoldingScores[i], names.get(i))
                    myHelper.updateTotalGame(arrHoldingScores[i] + lastTotalGameScores[i], names.get(i))
                    myHelper.updateNumOfTimesPlayed(numOfTimesPLayed[i] + 1 ,names.get(i))
                    val date = System.currentTimeMillis()
                    val sdf = SimpleDateFormat("hh-mm-ss")
                    val dateString = sdf.format(date)
                    var tempHis =  histories.get(i) + "  " +  arrHoldingScores[i] + "\n" + dateString
                    myHelper.updateHistory(tempHis ,names.get(i))
                    //clear the score value from the edit text
                    myCustomCursorAdapter.clearScoresFromEditText(binding.content.listview[i])
                    binding.content.listview[i].setBackgroundColor(getColor(R.color.white))
                }
                notifyDataChange()
                coloring()
            }else
                showToast("some scores are null !")
        }
    }

    private fun notifyDataChange() {
        myCustomCursorAdapter.changeCursor(myHelper.sortPlayersIncreasing())
        myCustomCursorAdapter.notifyDataSetChanged()
    }

    fun customCusorAdapter() {
        myCustomCursorAdapter = MyCustomCursorAdapter(applicationContext, myHelper.getPlayer())
        binding.content.listview.adapter = myCustomCursorAdapter
    }

    private fun coloring() {
        var g = binding.content.listview[0]
        g.setBackgroundColor(getColor(R.color.green))
        var r = binding.content.listview[binding.content.listview.size-1]
        r.setBackgroundColor(getColor(R.color.red))
        notifyDataChange()
    }

    private fun saveDataToDatabase() {
        val result = myHelper.addPlayer(
                binding.newPlayerEditText.text.toString(),
                0,
                0,
            0,
        )
        binding.content.apply {
            // get the data again from the database
            myCustomCursorAdapter.changeCursor(myHelper.getPlayer())
            myCustomCursorAdapter.notifyDataSetChanged()
        }
    }

    private fun clearText() {
        binding.content.apply {
            binding.newPlayerEditText.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.youtube -> {
                val face = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCOvnhBl71cp_Etk8kVyDbfw"))
                startActivity(face)
                true
            }

            R.id.delete_all -> {
                myHelper.deleteAllPlayer()
                myCustomCursorAdapter.changeCursor(myHelper.getPlayer())
                myCustomCursorAdapter.notifyDataSetChanged()
                showToast(getString(R.string.deleting_all_players_succ))
                true
            }

            R.id.hist -> {
                var intent = Intent(this , History::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var names = myHelper.getNames()
        for (i in 0 until binding.content.listview.size) {
            myHelper.deleteAllHistories(names[i])
        }
    }
}

