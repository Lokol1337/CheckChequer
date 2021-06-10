package com.example.checkchequer

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityAddMembers : AppCompatActivity() {

    private lateinit var array_members: MutableList<Member>
    private lateinit var members_adapter: MembersAdapter
    private lateinit var button_next: Button
    private lateinit var button_delete: ImageView
    private var flag_clear_focus: Boolean = false
    private lateinit var dataBaseHandler: DataBaseHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_members)
        initComponents()
    }

    private fun initComponents() {
        dataBaseHandler = DataBaseHandler(filesDir.toString())

        button_next = findViewById(R.id.activity_add_members_button_next)
        button_next.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
        button_delete = findViewById(R.id.activity_add_members_button_delete)
        button_delete.setOnClickListener(View.OnClickListener {
            members_adapter.deleteMember(this)
            members_adapter.notifyItemRemoved(members_adapter.itemCount)
        })

        val linearLayout = findViewById<LinearLayout>(R.id.activity_add_members_linear_layout_main)

        array_members = mutableListOf()
        members_adapter = MembersAdapter(linearLayout, array_members, button_next, button_delete)

        val linear_layout_manager = LinearLayoutManager(this)
        linear_layout_manager.orientation = LinearLayoutManager.VERTICAL

        val recyclerView: RecyclerView = findViewById(R.id.activity_add_members_RV)
        recyclerView.layoutManager = linear_layout_manager
        recyclerView.adapter = members_adapter

        if(intent.extras?.containsKey("repeated-meeting") == true) {
            val idMeeting: Int = this.intent.getStringExtra("repeated-meeting").toString().toInt()
            val meeting: DataBaseHandler.Meeting = dataBaseHandler.getMeeting(idMeeting)
            for (member in meeting.arrayMembers) {
                members_adapter.addMember(this, member.status, member.name)
                members_adapter.notifyItemChanged(members_adapter.itemCount - 1)
            }
        } else {
            members_adapter.addMember(this, true)
            members_adapter.notifyItemChanged(members_adapter.itemCount - 1)
        }
    }

    fun addNewMember (status: Boolean, name: String) {
        members_adapter.addMember(this, false, name)
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
        dataBaseHandler.addMeeting(array_members)
        val toASetStatus = Intent(this, ActivityScanOrWrite::class.java)
        startActivity(toASetStatus)
    }


    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()
}