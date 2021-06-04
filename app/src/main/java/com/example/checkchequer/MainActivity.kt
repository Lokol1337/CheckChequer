package com.example.checkchequer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonCreatingMeeting(view: View){
        val intent_creating_meeting = Intent(this@MainActivity, ActivityAddMembers::class.java)
        startActivity(intent_creating_meeting)
        /*setContentView(R.layout.activity_add_members)*/
    }
}