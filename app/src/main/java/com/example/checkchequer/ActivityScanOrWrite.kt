package com.example.checkchequer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator


class ActivityScanOrWrite : AppCompatActivity() {

    var array_products: MutableList<Product> = mutableListOf()
    var str_array_products_1: MutableList<String> = mutableListOf()
    /*var str_array_products_2: MutableList<String> = mutableListOf()*/
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>

    lateinit var editTextNameProduct: EditText
    lateinit var editTextPriceProduct: EditText
    lateinit var spinnerCount: Spinner
    lateinit var buttonNext: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_or_write)
        initComponents()
    }

    fun initComponents(){
        listView = findViewById<ListView>(R.id.activity_scan_or_write_list_view)
        adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, str_array_products_1)
        listView.adapter = adapter
        listView.isClickable = false

        editTextNameProduct = findViewById<EditText>(R.id.activity_scan_or_write_edit_text_name_product)
        editTextPriceProduct = findViewById<EditText>(R.id.activity_scan_or_write_edit_text_price_per_1)
        spinnerCount = findViewById<Spinner>(R.id.activity_scan_or_write_spinner_count)

        spinnerCount.setSelection(0)

        buttonNext = findViewById<Button>(R.id.activity_scan_or_write_button_next)
        buttonNext.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
        buttonNext.isClickable = false

        val buttonScan = findViewById<Button>(R.id.activity_scan_or_write_button_scan)
        buttonScan.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources
                .getDrawable(this, R.drawable.qr_scan), null, AppCompatResources
                .getDrawable(this, R.drawable.qr_scan), null)

    }

    fun ButtonAddProduct(view: View){

        editTextNameProduct.isFocusableInTouchMode = false
        editTextNameProduct.isFocusable = false
        editTextNameProduct.isFocusableInTouchMode = true
        editTextNameProduct.isFocusable = true

        editTextPriceProduct.isFocusableInTouchMode = false
        editTextPriceProduct.isFocusable = false
        editTextPriceProduct.isFocusableInTouchMode = true
        editTextPriceProduct.isFocusable = true

        val nameProduct = editTextNameProduct.text.toString()
        val priceProduct = editTextPriceProduct.text.toString()
        val count: Int = spinnerCount.selectedItem.toString().toInt()

        val textViewError = findViewById<TextView>(R.id.activity_scan_or_write_text_view_adding_error)

        textViewError.text = checkCorrectDataProduct(nameProduct, priceProduct)
        if (textViewError.text.toString().isEmpty()){
            val newProduct = Product(nameProduct, priceProduct.toInt(), count)
            array_products.add(newProduct)
            adapter.add(newProduct.toString())

            adapter.notifyDataSetChanged()

            editTextNameProduct.setText("")
            editTextPriceProduct.setText("")
            spinnerCount.setSelection(0)

            buttonNext.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            buttonNext.isClickable = true
        }
    }

    private fun checkCorrectDataProduct(nameProduct: String, priceProduct: String): String {
        return if (nameProduct != "" && priceProduct != "" && !isProductExist(nameProduct))
            ""
        else if (nameProduct != "" && priceProduct != "" && isProductExist(nameProduct))
            "(продукт с таким названием уже существует)"
        else if (nameProduct != "" && priceProduct == "" && !isProductExist(nameProduct))
            "(поле \"цены\" не введено)"
        else if (nameProduct == "" && priceProduct != "" && !isProductExist(nameProduct))
            "(поле \"названия продукта\" не заполнено)"
        else
            "(некоторые поля не заполнены)"
    }

    private fun isProductExist(nameProduct: String): Boolean{
        for (product in array_products)
            if (nameProduct == product.getName())
                return true
        return false
    }

    fun ButtonScan(view: View){
        //TODO(РЕАЛИЗОВАТЬ ПРОВЕРКУ НА ИНТЕРНЕТ)
        val scanner = IntentIntegrator(this)
        scanner.initiateScan()
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
                    val path = intent.getStringExtra("path").toString()
                    val intent = Intent(this, ActivityRequestScannerAPI::class.java)
                    intent.putExtra("text", result.contents.toString())
                    intent.putExtra("path", path)
                    startActivity(intent)
                }
            }
        }
    }

    fun ButtonNext(view: View){
        val intent = Intent(this, ActivityProductsAndMembers::class.java)
        val productsJSON = ProductsJSON()
        intent.putExtra("array-products", productsJSON.convertProductToJson(array_products))
        startActivity(intent)
    }

}