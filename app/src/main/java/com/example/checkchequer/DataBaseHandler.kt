package com.example.checkchequer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class DataBaseHandler {

    data class Meeting(
            val id: Int,
            val date: String,
            val countMembers: Int,
            var arrayMembers: MutableList<MemberJSON>,
            val stringOutput: String
    )

    data class MemberJSON(
            val name: String,
            val status: Boolean,
            val summ: Int
    )


    var MEMBERS_DATABASE_NAME: String
    var FILE_DIRECTORY: File
    var FILE: File
    var MEETING_LIST: MutableList<Meeting>
    var MEMBER_LIST: MutableList<MemberJSON>
    var _gson = Gson()
    val mutableListTutorialType = object : TypeToken<MutableList<Meeting>>() {}.type


    //private var ARRAY_MEMBERS: MutableList<Member> = mutableListOf()
    constructor(path: String) {
        MEMBERS_DATABASE_NAME = "FILE_NAME_MEMBERS.json"
        FILE_DIRECTORY = File(path, "FilesDB")
        FILE = File(FILE_DIRECTORY, MEMBERS_DATABASE_NAME)
        //FILE.createNewFile()
        if (!FILE.parentFile.exists())
            FILE.parentFile.mkdirs()
        if (!FILE.exists())
            FILE.createNewFile()
        MEETING_LIST = mutableListOf<Meeting>()
        MEMBER_LIST = mutableListOf<MemberJSON>()
        val jsonStr: String = FileInputStream(FILE).bufferedReader().use { it.readText() }
        if (jsonStr.isNotEmpty())
            try {
                MEETING_LIST = _gson.fromJson(jsonStr, mutableListTutorialType)
            } catch (e: Exception) {
                println("---- SMALL ERROR")
            }

        /*if (inputAsString == "" || inputAsString.isEmpty()){
            MEETING_LIST = mapper.readValue(inputAsString)
            MEMBER_LIST = MEETING_LIST[0].arrayMembers
        }*/

        /*try {
            MEETING_LIST = mapper.readValue(inputAsString)
            MEMBER_LIST = MEETING_LIST[0].arrayMembers
        } catch (e: Exception) {
            //e.printStackTrace()
            print("---- SMALL ERROR")
        }*/
    }

    fun writeTOFile(str: String) {
        val writer = PrintWriter(FILE)
        writer.print(str)
        writer.close()
    }

    fun addMeeting(members: MutableList<Member>) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        MEETING_LIST.add(Meeting(MEETING_LIST.size, sdf.format(Date()), members.size, mutableListOf<MemberJSON>(), ""))
        addMembers(members)
    }

    fun addMember(member: Member) {
        val m = MemberJSON(member.getName(), member.getStatus(), member.getSumm())
        MEETING_LIST[0].arrayMembers.add(m)
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutsArrayPretty: String = gsonPretty.toJson(MEETING_LIST)
        writeTOFile(jsonTutsArrayPretty)

        /*MEETING_LIST[0].arrayMembers.add(m)
        val jsonArray = mapper.writeValueAsString(MEETING_LIST)
        writeTOFile(jsonArray)*/
    }

    fun addMembers(members: MutableList<Member>) {
        for (member: Member in members) {
            val m = MemberJSON(member.getName(), member.getStatus(), member.getSumm())
            MEETING_LIST[0].arrayMembers.add(m)
        }
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutsArrayPretty: String = gsonPretty.toJson(MEETING_LIST)
        writeTOFile(jsonTutsArrayPretty)
    }


    fun getCountMembers(): Int {
        return MEETING_LIST[0].countMembers
    }

    fun getMember(id: Int): Member {
        val member: Member = Member()
        member.setName(MEETING_LIST[0].arrayMembers[id].name)
        member.setStatus(MEETING_LIST[0].arrayMembers[id].status)
        member.setSumm(MEETING_LIST[0].arrayMembers[id].summ)
        return member
    }

    fun getMember(name: String): Member? {
        var id = -1
        var count = 1
        for (member: MemberJSON in MEETING_LIST[0].arrayMembers) {
            if (member.name == name) {
                id = count
                break
            }
            count++
        }
        if (id == -1)
            return null
        return getMember(id)
    }


    fun getAllMembers(): MutableList<Member> {
        val listMembers: MutableList<Member> = mutableListOf()
        for (i: Int in 0..getCountMembers()) {
            val member: Member = Member()
            member.setName(MEETING_LIST[0].arrayMembers[i].name)
            member.setStatus(MEETING_LIST[0].arrayMembers[i].status)
            member.setSumm(MEETING_LIST[0].arrayMembers[i].summ)
            listMembers.add(member)
        }
        return listMembers
    }

    fun memberIsExist(member: Member): Boolean {
        for (m: MemberJSON in MEETING_LIST[0].arrayMembers) {
            if (m.name == member.getName() && m.status == member.getStatus() && m.summ == member.getSumm())
                return true
        }
        return false
    }

    fun cleanDB() {
        writeTOFile("")
        MEETING_LIST = mutableListOf<Meeting>()
        MEMBER_LIST = mutableListOf<MemberJSON>()
    }

    fun stringAllUsers(): String {
        var str: String = ""
        for (m in MEMBER_LIST) {
            str += m.name + ":  " + m.status + "  " + m.summ + "\n"
        }
        return str
    }

}