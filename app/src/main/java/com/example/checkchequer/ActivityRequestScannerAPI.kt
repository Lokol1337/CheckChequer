package com.example.checkchequer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.AssertionError

class ActivityRequestScannerAPI : AppCompatActivity() {

//    private lateinit var scannerService: RetrofitServices
    lateinit var dataBaseHandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_scanner_api)
        initComponents()
    }

    fun initComponents(){
        val path = intent.getStringExtra("path").toString()
        dataBaseHandler = DataBaseHandler(path)
        println("\n---- ALL USERS: " + dataBaseHandler.stringAllUsers())

        val textView = findViewById<TextView>(R.id.xer)
        val text: String = intent.getStringExtra("text").toString()
        val list_positions: List<Item> = GetCheckFromAPI(text)
        if (list_positions.isEmpty()){
            val intent_to_scan_or_write = Intent(this, ActivityScanOrWrite::class.java)
            startActivity(intent_to_scan_or_write)
        }

        var result = ""
        for (position in list_positions){
            result += position.toString()
        }
        textView.text = result

    }

    fun GetCheckFromAPI(text: String?): List<Item>{
        var list_positions: List<Item> = mutableListOf()
        val retrofit: RetrofitClient = RetrofitClient
        val scannerService: RetrofitServices = retrofit.getClient(
            "https://scanner-oleg.herokuapp.com").create(RetrofitServices::class.java)
        if (text != null) {
            scannerService.getCheck().enqueue(object : Callback<List<Item>>{
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    list_positions = response.body()!!
                }
                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    throw Exception("SERVER ERROR")
                    println("!!!! SERVER ERROR -----------------------------------")
                }
            })
        }
        return list_positions
    }

    object RetrofitClient {
        private var retrofit: Retrofit? = null

        fun getClient(baseUrl: String): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }

    interface RetrofitServices {
        @GET("/check")
        fun getCheck(): Call<List<Item>>
    }

}


class Item {
    var sum: Int? = null
    var price: Int? = null
    var quantity: Int? = null
    var name: String = ""

    override fun toString(): String {
        return "Name = $name :\n\tSum = $sum\n\tPrice = $price\n\tQuantity = $quantity"
    }
}