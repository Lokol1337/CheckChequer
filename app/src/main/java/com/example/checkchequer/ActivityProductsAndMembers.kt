package com.example.checkchequer

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class ActivityProductsAndMembers : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>
    var arrayMembers: MutableList<Member> = mutableListOf()
    var arrayProducts: MutableList<Product> = mutableListOf()
    var str_arrayProducts: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_and_members)
        initComponents()
    }

    fun initComponents(){
        val productsJSON = ProductsJSON()
        val path = filesDir.toString()
        val dataBaseHandler = DataBaseHandler(path)
        arrayMembers = dataBaseHandler.getAllMembers()

        val extra_productsJSON: String = intent.getStringExtra("array-products").toString()
        arrayProducts = productsJSON.convertProductToArray(extra_productsJSON)
        for (product in arrayProducts){
            str_arrayProducts.add(product.getName())
        }

        listView = findViewById<ListView>(R.id.activity_products_and_members_list_view)
        adapter = ArrayAdapter<String>(this, R.layout.simple_list_item, str_arrayProducts)
        listView.adapter = adapter

        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, l ->
            val arrayMembersBoolean: MutableList<Boolean> = mutableListOf()
            val str_arrayMembers: MutableList<String> = mutableListOf()
            for (i in 0 until arrayMembers.size){
                str_arrayMembers.add(arrayMembers[i].getName())
                if (isInArray(arrayMembers[i], arrayProducts[position])){
                    arrayMembersBoolean.add(true)
                } else
                    arrayMembersBoolean.add(false)
            }
            val myDialogFragment = MyDialogFragment(arrayMembers, arrayProducts, arrayMembersBoolean, str_arrayMembers, position)
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            myDialogFragment.show(transaction, "dialog")
        })
    }

    fun isInArray(member: Member, product: Product): Boolean{
        for (pr in member.getArrayProducts()) {
            if (product.getName() == pr.getName())
                return true
        }
        return false
    }

    fun ButtonNext(view: View){
        val result = calculation()
        println("----\n$result")
        val intent = Intent(this, ActivityResult::class.java)
        intent.putExtra("result", result)
        startActivity(intent)
    }

    fun calculation(): String{
        var result = ""
        for ((i, member) in arrayMembers.withIndex()){
            if (!member.getStatus()) {
                var sum: Float = 0.toFloat()
                val products: MutableList<Product> = member.getArrayProducts()
                for (product in products) {
                    sum += product.getSumm().toFloat() / product.getCount()
                }
                result += member.getName() + " --> " + sum.toInt() + " р."
                member.setSumm(sum.toInt())
            }
            else
                result += member.getName() + " --> 0 р."
            if (i != arrayMembers.size)
                result += "\n"
        }
        return result
    }
}

class MyDialogFragment(
    var arrayMembers: MutableList<Member>,
    var arrayProducts: MutableList<Product>,
    var arrayMembersBoolean: MutableList<Boolean>,
    var str_arrayMembers: MutableList<String>,
    var position: Int
) : DialogFragment() {

    //private val catNames = arrayOf("Васька", "Рыжик", "Мурзик")
    private val checkedItems: BooleanArray = arrayMembersBoolean.toBooleanArray()
    private val array: Array<String> = str_arrayMembers.toTypedArray()
    private val nameCurrentProduct: String = arrayProducts[position].getName()
    private val priceCurrentProduct: Int = arrayProducts[position].getPrice()
    private val countCurrentProduct: Float = arrayProducts[position].getCount()
    private var countChecked: Int = 0
    private val summCurrentProduct: Int = arrayProducts[position].getSumm()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите пользователей")
                .setMultiChoiceItems(array, checkedItems) { dialog, which, isChecked ->
                    checkedItems[which] = isChecked
                    val name = array[which] // Get the clicked item
                    //Toast.makeText(activity, name, Toast.LENGTH_LONG).show()
                }
                .setPositiveButton("Готово") { dialog, id ->
                    for (i in array.indices) {
                        val checked = checkedItems[i]
                        if (checked) {
                            countChecked++
                        }
                    }
                    for (i in array.indices) {
                        val checked = checkedItems[i]
                        if (checked) {
                            arrayMembers[i].addProduct(
                                Product(nameCurrentProduct, priceCurrentProduct,
                                    /*countCurrentProduct.toFloat() / */countChecked.toFloat(), summCurrentProduct))
                            Log.i("Dialog", array[i])
                        }
                    }
                }
                .setNegativeButton("Отмена") { dialog, _ ->

                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}