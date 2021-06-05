package com.example.checkchequer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ActivityAddMembers : AppCompatActivity() {

    private var array_members: MutableList<Member> = mutableListOf()
    private lateinit var members_adapter: MembersAdapter
    private lateinit var button_next: Button
    private lateinit var listView: ListView
    private lateinit var listViewAdapter: ArrayAdapter<Member>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_members)
        initComponents()
    }

    private fun initComponents(){
        button_next = findViewById(R.id.activity_add_members_button_next)
        button_next.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))

        /*array_members = mutableListOf()
        members_adapter = MembersAdapter(array_members, button_next)

        val linear_layout_manager = LinearLayoutManager(this)
        linear_layout_manager.orientation = LinearLayoutManager.VERTICAL

        val recyclerView: RecyclerView = findViewById(R.id.activity_add_members_RV)
        recyclerView.layoutManager = linear_layout_manager
        recyclerView.adapter = members_adapter*/

        Collections.addAll(array_members)

        listView = findViewById<ListView>(R.id.activity_add_members_LV)
        listView.itemsCanFocus = true

        listViewAdapter = ArrayAdapter(this, R.layout.listview_item_name_status, array_members)
        listView.adapter = listViewAdapter

    }

    fun addNewMember(view: View) {
        /*members_adapter.addMember(this)
        members_adapter.notifyItemChanged(members_adapter.itemCount - 1)*/
        val editTextName = findViewById<EditText>(R.id.recycle_item_name_status_edit_text_name)
        val buttonStatus = findViewById<ImageView>(R.id.recycle_item_name_status_image_view_status)
        if (array_members.size == 0) {
            listViewAdapter.add(Member("", true))
            editTextName.hint = "Оплативший"
            buttonStatus.setImageResource(R.drawable.money)
        }
        else {
            listViewAdapter.add(Member("", false))
            buttonStatus.setImageResource(R.drawable.gray_no_money)
        }
        buttonStatus.isClickable = false
        listViewAdapter.notifyDataSetChanged();
    }

    fun deleteLastMember(view: View) {
        //TODO(РЕАЛИЗОВАТЬ УДАЛЕНИЕ ПОСЛЕДНЕЙ КАРТОЧКИ)
    }

    fun buttonToASetStatus(view: View){
        val filesDir = this.filesDir.toString()
        val dataBaseHandler = DataBaseHandler(filesDir)
        dataBaseHandler.cleanDB()
        dataBaseHandler.addMeeting(array_members)
        val toASetStatus = Intent(this, ActivityScanOrWrite::class.java).apply {
            putExtra("path", filesDir)
        }
        startActivity(toASetStatus)
    }


    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()
}