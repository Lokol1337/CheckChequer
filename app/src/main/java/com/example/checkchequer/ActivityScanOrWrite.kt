package com.example.checkchequer

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

class ActivityScanOrWrite : AppCompatActivity() {

    //lateinit var dataBaseHandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_or_write)
        initComponents()
    }

    fun initComponents(){
        /*val path: String = intent.getStringExtra("path").toString()
        dataBaseHandler = DataBaseHandler(path)
        println("\n---- ALL USERS: " + dataBaseHandler.stringAllUsers())*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Something wrong", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Scanned" + result.contents, Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ActivityRequestScannerAPI::class.java)
                    intent.putExtra("text", result.contents.toString())
                    startActivity(intent)
                }
            }
        }
    }

    fun ButtonScan(view: View){
        val scanner = IntentIntegrator(this)
        scanner.initiateScan()
    }

}