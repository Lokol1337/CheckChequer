package com.example.checkchequer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter


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

    var mapper = jacksonObjectMapper()
    var MEMBERS_DATABASE_NAME: String
    var MEMBERS_DATABASE_PATH: String
    var FILE_DIRECTORY: File
    var FILE: File
    var MEETING_LIST: MutableList<Meeting>
    var MEMBER_LIST: MutableList<MemberJSON>
    var PATH: String


    //private var ARRAY_MEMBERS: MutableList<Member> = mutableListOf()
    constructor(path: String) {
        PATH = path
        MEMBERS_DATABASE_NAME = "FILE_NAME_MEMBERS.json"
        MEMBERS_DATABASE_PATH = path
        FILE_DIRECTORY = File(MEMBERS_DATABASE_PATH, "FilesDB")
        FILE_DIRECTORY.mkdirs()
        FILE = File(FILE_DIRECTORY, MEMBERS_DATABASE_NAME)
        FILE.createNewFile()
        MEETING_LIST = mutableListOf<Meeting>()
        MEMBER_LIST = mutableListOf<MemberJSON>()
        val inputAsString = FileInputStream(FILE).bufferedReader().use { it.readText() }
        try {
            MEETING_LIST = mapper.readValue(inputAsString)
            MEMBER_LIST = MEETING_LIST[0].arrayMembers
        } catch (e: Exception) {
            //e.printStackTrace()
            print("---- SMALL ERROR")
        }
    }

    fun writeTOFile(str: String) {
        val writer = PrintWriter(FILE)
        writer.print(str)
        writer.close()
    }

    fun addMeeting(members: MutableList<Member>) {
        MEETING_LIST.add(Meeting(MEETING_LIST.size, "", members.size, mutableListOf<MemberJSON>(), ""))
        addMembers(members)
    }

    fun addMember(member: Member) {
        val m = MemberJSON(member.getName(), member.getStatus(), member.getSumm())
        MEETING_LIST[0].arrayMembers.add(m)
        val jsonArray = mapper.writeValueAsString(MEETING_LIST)
        writeTOFile(jsonArray)
    }

    fun addMembers(members: MutableList<Member>) {
        for (member: Member in members) {
            val m = MemberJSON(member.getName(), member.getStatus(), member.getSumm())
            MEETING_LIST[0].arrayMembers.add(m)
        }
        val jsonArray = mapper.writeValueAsString(MEETING_LIST)
        writeTOFile(jsonArray)
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