package com.example.checkchequer

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RequestScannerAPI {

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

    fun GetCheckFromAPI(): String{
        var list_positions: List<Item> = mutableListOf()
        val retrofit: RetrofitClient = RetrofitClient
        val scannerService: RetrofitServices = retrofit.getClient(
                "https://scanner-oleg.herokuapp.com").create(RetrofitServices::class.java)

        scannerService.getCheck().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                list_positions = response.body()!!
                var result = ""
                for (position in list_positions){
                    result += position.toString()
                }
                //textView.text = result
                println(result)
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                throw Exception("SERVER ERROR")
                println("!!!! SERVER ERROR -----------------------------------")
            }
        })

        return ""
    }

}

/*class Item {
    var sum: Int? = null
    var price: Int? = null
    var quantity: Int? = null
    var name: String = ""

    override fun toString(): String {
        return "Name = $name :\n\tSum = $sum\n\tPrice = $price\n\tQuantity = $quantity"
    }
}*/