package com.example.checkchequer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.TextView

class ActivityResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        initComponents()
    }

    private fun initComponents() {
        val textView = findViewById<TextView>(R.id.activity_result_text_view_result)
        val result = intent.getStringExtra("result").toString()
        textView.text = result

        val path: String = filesDir.toString()
        val dataBaseHandler: DataBaseHandler = DataBaseHandler(path)
        dataBaseHandler.addStringOutput(result)
    }

    fun ButtonNext(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}