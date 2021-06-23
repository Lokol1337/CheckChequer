package com.example.checkchequer

import android.content.Context
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
import retrofit2.http.Query


class ActivityRequestScannerAPI : AppCompatActivity() {


    //IP локального сервера
    val API_LOCAL_URL = "http://192.168.1.145:8070"

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_scanner_api)
        initComponents()
    }

    fun initComponents(){
        context = this

        val textView = findViewById<TextView>(R.id.xer)
        val text: String = intent.getStringExtra("text").toString()

        var list_positions: List<Item> = mutableListOf()
        val retrofit: RetrofitClient = RetrofitClient
        val scannerService: RetrofitServices = retrofit.getClient(API_LOCAL_URL)
                .create(RetrofitServices::class.java)

        scannerService.getCheck(text).enqueue(object : Callback<List<Item>>{
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                list_positions = response.body()!!
                var result = ""
                for (position in list_positions){
                    result += position.toString()
                }
                //textView.text = result
                println(result)

                val arrayProducts: MutableList<Product> = mutableListOf()
                for (item in list_positions){
                    var quant: Float = item.quantity
                    if (quant < 1)
                        quant = 1.0F
                    arrayProducts.add(Product(item.name, item.price/100, quant, item.sum/100))
                }

                val intent = Intent(context, ActivityProductsAndMembers::class.java)
                val productsJSON = ProductsJSON()
                //val path: String = intent.getStringExtra("path").toString()
                intent.putExtra("array-products", productsJSON.convertProductToJson(arrayProducts))
                startActivity(intent)
            }
            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                throw Exception("SERVER ERROR")
                println("!!!! SERVER ERROR -----------------------------------")
            }
        })


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
        fun getCheck(@Query("checkString") checkString : String): Call<List<Item>>
    }

}


class Item {
    var sum: Int = 0
    var price: Int = 0
    var quantity: Float = 0F
    var name: String = ""

    override fun toString(): String {
        return "Name = $name :\n\tSum = $sum\n\tPrice = $price\n\tQuantity = $quantity"
    }
}
