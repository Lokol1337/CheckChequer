package com.example.checkchequer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class ActivityMeeting : AppCompatActivity() {

    lateinit var listView: ListView
    var arrayMembers: MutableList<Member> = mutableListOf()
    var str_arrayMembers: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)
        initComponents()
    }

    private fun initComponents() {
        val idMeeting: Int = intent.getStringExtra("index-meeting").toString().toInt()
        val path = filesDir.toString()
        val dataBaseHandler: DataBaseHandler = DataBaseHandler(path)
        val meeting: DataBaseHandler.Meeting = dataBaseHandler.getMeeting(idMeeting)

        for (member in meeting.arrayMembers) {
            arrayMembers.add(Member(member.name, member.status, member.summ))
            str_arrayMembers.add(member.name)
        }

        val textViewName = findViewById<TextView>(R.id.activity_meeting_text_view_name_meeting)
        textViewName.text = "ВСТРЕЧА: ${meeting.date}"

        val textViewResult = findViewById<TextView>(R.id.activity_meeting_text_view_result)
        textViewResult.text = meeting.stringOutput

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.simple_list_item, str_arrayMembers)
        listView = findViewById(R.id.activity_meeting_list_view)
        listView.adapter = adapter

    }

    fun ButtonRepeatMeeting(view: View){

    }
}