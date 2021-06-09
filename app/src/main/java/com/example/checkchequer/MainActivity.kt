package com.example.checkchequer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    var str_array_meeting: MutableList<String> = mutableListOf()
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        val db = DataBaseHandler(filesDir.toString())
        db.cleanDB()
        val dataBaseHandler = DataBaseHandler(filesDir.toString())
        val meetingList: MutableList<DataBaseHandler.Meeting> = dataBaseHandler.getAllMeetings()
        for (meeting in meetingList){
            str_array_meeting.add((meeting.id + 1).toString() + ": " + meeting.date)
        }

        listView = findViewById<ListView>(R.id.activity_main_list_view)
        adapter = ArrayAdapter<String>(this, R.layout.simple_list_item, str_array_meeting)
        listView.adapter = adapter
        listView.isClickable = false
    }

    fun buttonCreatingMeeting(view: View){
        val intent_creating_meeting = Intent(this@MainActivity, ActivityAddMembers::class.java)
        startActivity(intent_creating_meeting)
        /*setContentView(R.layout.activity_add_members)*/
    }
}