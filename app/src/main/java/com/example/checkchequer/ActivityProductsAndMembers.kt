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
                arrayMembersBoolean.add(false)
                str_arrayMembers.add(arrayMembers[i].getName())
            }
            val myDialogFragment = MyDialogFragment(arrayMembers, arrayProducts, arrayMembersBoolean, str_arrayMembers, position)
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            myDialogFragment.show(transaction, "dialog")
        })
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
        for (member in arrayMembers){
            if (!member.getStatus()) {
                var sum: Float = 0.toFloat()
                val products: MutableList<Product> = member.getArrayProducts()
                for (product in products) {
                    sum += product.getSumm().toFloat() / product.getCount()
                }
                result += member.getName() + " --> " + sum.toInt() + " р.\n"
            }
        }
        return result
    }

    /*private class MyCustomAdapter(context: Context, ): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        // responsible for how many rows in my list
        override fun getCount(): Int {
            return 5
        }

        // you can also ignore this
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        // you can ignore this for now
        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val textView = TextView(mContext)
            textView.text =
            return textView
        }

    }*/
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
    private val countCurrentProduct: Int = arrayProducts[position].getCount()
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