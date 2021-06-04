package com.example.checkchequer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivitySetStatus : AppCompatActivity() {

    lateinit var dataBaseHandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_status)
        initComponents()
    }

    fun initComponents(){
        val path: String = intent.getStringExtra("path").toString()
        dataBaseHandler = DataBaseHandler(path)
    }

}