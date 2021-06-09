package com.example.checkchequer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    var str_array_products_1: MutableList<String> = mutableListOf()
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        /*val dataBaseHandler: DataBaseHandler = DataBaseHandler(filesDir.toString())

        listView = findViewById<ListView>(R.id.activity_main_list_view)
        adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, str_array_products_1)
        listView.adapter = adapter
        listView.isClickable = false*/
    }

    fun buttonCreatingMeeting(view: View){
        val intent_creating_meeting = Intent(this@MainActivity, ActivityAddMembers::class.java)
        startActivity(intent_creating_meeting)
        /*setContentView(R.layout.activity_add_members)*/
    }
}