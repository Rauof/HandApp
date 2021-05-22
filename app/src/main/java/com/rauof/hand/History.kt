package com.rauof.hand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rauof.hand.databinding.ActivityListBinding

class History : AppCompatActivity() {
    private lateinit var historAdapter:HistoryAdapter
    private lateinit var myHelper: MySqlHelper
    private lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myHelper = MySqlHelper(applicationContext)
        customCursorAdapter()
    }

    private fun customCursorAdapter() {
        historAdapter = HistoryAdapter(applicationContext, myHelper.getPlayerForHistory())
        binding.content.listview.adapter = historAdapter
    }
}