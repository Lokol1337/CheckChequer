package com.example.checkchequer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityAddMembers : AppCompatActivity() {

    private lateinit var array_members: MutableList<Member>
    private lateinit var members_adapter: MembersAdapter
    private lateinit var button_next: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_members)
        initComponents()
    }

    private fun initComponents() {
        button_next = findViewById(R.id.activity_add_members_button_next)
        button_next.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))

        array_members = mutableListOf()
        members_adapter = MembersAdapter(array_members, button_next)

        val linear_layout_manager = LinearLayoutManager(this)
        linear_layout_manager.orientation = LinearLayoutManager.VERTICAL

        val recyclerView: RecyclerView = findViewById(R.id.activity_add_members_RV)
        recyclerView.layoutManager = linear_layout_manager
        recyclerView.adapter = members_adapter

        members_adapter.addMember(this, true)
        members_adapter.notifyItemChanged(members_adapter.itemCount - 1)
    }

    fun addNewMember(view: View) {
        members_adapter.addMember(this, false)
        members_adapter.notifyItemChanged(members_adapter.itemCount - 1)
    }

    fun deleteLastMember(view: View) {
        //TODO(НЕ РАБОТАЕТ, когда на последнем элементе фокус -> нужно вставлять кнопку удаления в сам элемент)
        members_adapter.deleteMember(this)
        members_adapter.notifyItemRemoved(members_adapter.itemCount)
    }

    fun buttonToASetStatus(view: View) {
        val filesDir = filesDir.toString()
        val dataBaseHandler = DataBaseHandler(filesDir)
        dataBaseHandler.cleanDB()
        dataBaseHandler.addMeeting(array_members)
        val toASetStatus = Intent(this, ActivityScanOrWrite::class.java)
        startActivity(toASetStatus)
    }


    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()
}