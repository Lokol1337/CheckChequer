package com.example.checkchequer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class ActivityProductsAndMembers : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>
    var arrayProducts: MutableList<Product> = mutableListOf()
    var str_arrayProducts: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_and_members)
        initComponents()
    }

    fun initComponents(){
        val productsJSON = ProductsJSON()

        val extra_productsJSON: String = intent.getStringExtra("array-products").toString()
        arrayProducts = productsJSON.convertProductToArray(extra_productsJSON)
        for (product in arrayProducts){
            str_arrayProducts.add(product.getName())
        }

        listView = findViewById<ListView>(R.id.activity_products_and_members_list_view)
        adapter = ArrayAdapter<String>(this, R.layout.simple_list_item, str_arrayProducts)
        listView.adapter = adapter
    }
}