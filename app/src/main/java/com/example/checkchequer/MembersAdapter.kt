package com.example.checkchequer

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class MembersAdapter(val linearLayout: LinearLayout, var array_members: MutableList<Member>, var button_next: Button, var button_delete: ImageView)
    : RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {

    private var flag_filled_edit_texts: Boolean = false
    private var members_size: Int = 0
    private var count_payer = 0
    private lateinit var context: Context

    init {
        button_next.isClickable = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        this.context = parent.context
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_item_name_status, parent, false)
        val new_member_view_holder = MemberViewHolder(itemView)
        return new_member_view_holder
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(this.array_members[position])
        println("----------------------  $position")
    }

    override fun getItemCount(): Int {
        return members_size
    }

    fun addMember(context: Context, flag: Boolean) {
        members_size++
        array_members.add(Member("", flag))
        flag_filled_edit_texts = false
        setButtonNPClickable(context, false)
    }

    fun addMember(context: Context, flag: Boolean, name: String) {
        members_size++
        array_members.add(Member(name, flag))
        flag_filled_edit_texts = true
        if (members_size - 1 == 0)
            setButtonNPClickable(context, true)
    }

    fun deleteMember(context: Context){
        if(members_size > 1){
            array_members.removeAt(members_size - 1)
            members_size--
            //notifyDataSetChanged()
        }
    }

    fun setButtonNPClickable(context: Context, flag: Boolean) {
        button_next.isClickable = flag
        if (flag)
            button_next.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        else
            button_next.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey))
    }

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name_edit_text: EditText = view.findViewById<View>(R.id.recycle_item_name_status_edit_text_name) as EditText
        var status_button: ImageView = view.findViewById(R.id.recycle_item_name_status_image_view_status) as ImageView

        fun checkFields() {
            //println("\n---- MEMBERS:")
            flag_filled_edit_texts = true
            for (m in array_members) {
                if (m.getName() == "")
                    flag_filled_edit_texts = false
                //println("+ " + m.getName())
            }
            if (flag_filled_edit_texts && members_size > 1)
                setButtonNPClickable(context, true)
            else
                setButtonNPClickable(context, false)
        }

        fun bind(member: Member) {

            if (member.getStatus()) {
                this.status_button.setImageResource(R.drawable.money)
                this.name_edit_text.hint = "Оплативший"
                count_payer++
            } else
                this.status_button.setImageResource(R.drawable.gray_no_money)
            this.status_button.isClickable = false

            if (member.getName().isNotEmpty()){
                this.name_edit_text.setText(member.getName())
            }

            checkFields()

            this.name_edit_text.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    button_delete.isClickable = false
                    setButtonNPClickable(context, false)
                    linearLayout.viewTreeObserver.addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {

                                val r = Rect()
                                linearLayout.getWindowVisibleDisplayFrame(r)
                                val screenHeight: Int = linearLayout.getRootView().getHeight()

                                val keypadHeight: Int = screenHeight - r.bottom

                                if (keypadHeight > screenHeight * 0.15) {
                                    // keyboard is opened
                                    Log.e("ActivityAddMembers", "keyboard opened")
                                } else {
                                    // keyboard is closed
                                    Log.e("ActivityAddMembers", "keyboard closed")
                                    view.isFocusable = false
                                    view.isFocusableInTouchMode = false
                                    view.isFocusable = true
                                    view.isFocusableInTouchMode = true
                                }
                            }
                        })
                    //flag_edit_text_focus = true
                }
                else {
                    button_delete.isClickable = true
                    //flag_edit_text_focus = true
                    if (member.getName() != this.name_edit_text.text.toString())
                        member.setName(this.name_edit_text.text.toString())
                    checkFields()
                    println("---- EDIT_TEXT :: " + this.name_edit_text.text.toString() + "\n" +
                            "---- STATUS_MEMBER :: " + member.getStatus() + "\n" +
                            "---- NAME_MEMBER :: " + member.getName() + "\n" +
                            "---- FLAG_FILLED :: " + flag_filled_edit_texts + "\n" + context)
                }
            }

            this.name_edit_text.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    member.setName(this.name_edit_text.text.toString())
                    checkFields()
                    println("---- EDIT_TEXT :: " + this.name_edit_text.text.toString() + "\n" +
                            "---- STATUS_MEMBER :: " + member.getStatus() + "\n" +
                            "---- NAME_MEMBER :: " + member.getName() + "\n" +
                            "---- FLAG_FILLED :: " + flag_filled_edit_texts + "\n" + context)
                    this.name_edit_text.isFocusableInTouchMode = false
                    this.name_edit_text.isFocusable = false
                    this.name_edit_text.isFocusableInTouchMode = true
                    this.name_edit_text.isFocusable = true
                    true
                } else
                    false
            }
        }
    }

}