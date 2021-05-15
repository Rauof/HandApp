package com.rauof.hand

import android.content.Intent
import android.graphics.Color.green
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import com.rauof.hand.databinding.ActivityListBinding
import java.text.SimpleDateFormat
import java.util.*

class ListActivity : AppCompatActivity() {

    private lateinit var myCustomCursorAdapter:MyCustomCursorAdapter
    private lateinit var myHelper: MySqlHelper
    private lateinit var binding: ActivityListBinding
    private var history = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myHelper = MySqlHelper(applicationContext)

        customCusorAdapter()
        for (i in 0 until binding.content.listview.size){
            history.add(myCustomCursorAdapter.getTotalGames(binding.content.listview[i]))
        }

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
                //update all players' scores, last games and total scores
                for (i in 0 until binding.content.listview.size) {
                    //update data for the player with index i
                    myHelper.updateLastGame(arrHoldingScores[i], i + 1)
                    myHelper.updateTotalGame(arrHoldingScores[i] + lastTotalGameScores[i], i + 1)
                    myHelper.updateNumOfTimesPlayed(numOfTimesPLayed[i] + 1 ,i+1)
                    //clear the score value from the edit text
                    myCustomCursorAdapter.clearScoresFromEditText(binding.content.listview[i])
                }
                notifyDataChange()
            }else
                showToast("some scores are null !")
        }
    }

    private fun notifyDataChange() {
//        myCustomCursorAdapter.changeBackgrounds(binding.content.listview[0] ,
//            binding.content.listview[binding.content.listview.size])

        myCustomCursorAdapter.changeCursor(myHelper.sortPlayersDecreasing())
        binding.content.listview[0].setBackgroundColor(getColor(R.color.green))
        binding.content.listview[binding.content.listview.size-1].setBackgroundColor(getColor(R.color.red))
        myCustomCursorAdapter.notifyDataSetChanged()
    }

    fun customCusorAdapter() {
        myCustomCursorAdapter = MyCustomCursorAdapter(applicationContext, myHelper.getPlayer())
        binding.content.listview.adapter = myCustomCursorAdapter
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

            R.id.trying -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

}

