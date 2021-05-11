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
                0,
                "New player")
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
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }

}
