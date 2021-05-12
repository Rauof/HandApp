package com.rauof.hand

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import com.rauof.hand.databinding.ActivityListBinding

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
            if(binding.newplayerEditText.text.isNotEmpty()){
                SaveDataToDatabase()
                clearText()
            }
            else
                showToast(getString(R.string.empty_player_name))
        }

        findViewById<Button>(R.id.calc).setOnClickListener {
            var arr_holding_scores = IntArray(binding.content.listview.size)
            var isAnyScoreNull = false
            for (i in 0 until binding.content.listview.size){
                if(myCustomCursorAdapter.getScoreFromEditText(binding.content.listview[i]).isNotEmpty())
                    arr_holding_scores[i] = myCustomCursorAdapter.getScoreFromEditText(binding.content.listview[i]).toInt()
                else
                    isAnyScoreNull = true;
            }
            if(!isAnyScoreNull){
                for (i in 0 until binding.content.listview.size) {
                    myHelper.updateData(arr_holding_scores[i], i+1)
                    myCustomCursorAdapter.clearScoresFromEditText(binding.content.listview[i])
                }
                myCustomCursorAdapter.changeCursor(myHelper.getPlayer())
                myCustomCursorAdapter.notifyDataSetChanged()
            }else
                showToast("Some scores are null")
        }
    }

    fun customCusorAdapter() {
        myCustomCursorAdapter = MyCustomCursorAdapter(applicationContext, myHelper.getPlayer())
        binding.content.listview.adapter = myCustomCursorAdapter
    }

    private fun SaveDataToDatabase() {
        val result = myHelper.addPlayer(
                binding.newplayerEditText.text.toString(),
                0,
                0)
        binding.content.apply {
            // get the data again from the database
            myCustomCursorAdapter.changeCursor(myHelper.getPlayer())
            myCustomCursorAdapter.notifyDataSetChanged()
        }
    }

    private fun clearText() {
        binding.content.apply {
            binding.newplayerEditText.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

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

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

}
