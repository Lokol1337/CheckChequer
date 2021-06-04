package com.example.checkchequer

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class MembersAdapter(var array_members: MutableList<Member>, var button_next: Button) : RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {

    private var flag_filled_edit_texts: Boolean = false
    private var members_size: Int = 0
    private var count_payer = 0
    private lateinit var context: Context
    //private lateinit var dbHandler: DataBaseHandler

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
    }

    override fun getItemCount(): Int {
        return members_size
    }

    fun addMember(context: Context) {
        members_size++
        array_members.add(Member("", false))
        flag_filled_edit_texts = false
        setButtonNPClickable(context, false)
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
        //var name_text_input_layout: TextInputLayout = view.findViewById(R.id.recycle_item_name_status_text_input_layout)

        fun checkFields() {
            //println("\n---- MEMBERS:")
            flag_filled_edit_texts = true
            for (m in array_members) {
                if (m.getName() == "")
                    flag_filled_edit_texts = false
                //println("+ " + m.getName())
            }
            if (flag_filled_edit_texts && count_payer > 0 && members_size > 1)
                setButtonNPClickable(context, true)
            else
                setButtonNPClickable(context, false)
        }

        fun bind(member: Member) {

            this.status_button.setOnClickListener {
                if (member.getStatus()) {
                    this.status_button.setImageResource(R.drawable.gray_no_money)
                    member.setStatus(false)
                    count_payer--
                } else {
                    this.status_button.setImageResource(R.drawable.money)
                    member.setStatus(true)
                    count_payer++
                }
                checkFields()
            }

            this.name_edit_text.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    member.setName(this.name_edit_text.text.toString())
                    checkFields()
                    println("---- EDIT_TEXT :: " + this.name_edit_text.text.toString() + "\n" +
                            "---- STATUS_MEMBER :: " + member.getStatus() + "\n" +
                            "---- NAME_MEMBER :: " + member.getName() + "\n" +
                            "---- FLAG_FILLED :: " + flag_filled_edit_texts + "\n" + context)
                }
            }


        }
    }

}