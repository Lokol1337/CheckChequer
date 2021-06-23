package com.example.checkchequer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*


class DataBaseHandler {

    data class Meeting(
        val id: Int,
        val date: String,
        var countMembers: Int,
        var arrayMembers: MutableList<MemberJSON>,
        var stringOutput: String
    )

    data class MemberJSON(
            val name: String,
            val status: Boolean,
            val summ: Int
    )

    data class ProductJSON(
            val name: String,
            val price: Int,
            val count: Float,
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
        if (jsonStr.isNotEmpty()) {
            try {
                MEETING_LIST = _gson.fromJson(jsonStr, mutableListTutorialType)
            } catch (e: Exception) {
                println("---- SMALL ERROR")
            }
        }
        /*else
            addMeeting(mutableListOf())*/
    }

    fun writeTOFile(str: String) {
        val writer = PrintWriter(FILE)
        writer.print(str)
        writer.close()
    }


    //---- ADDERS
    fun addMeeting(members: MutableList<Member>) {
        val sdf = stringData()
        MEETING_LIST.add(Meeting(MEETING_LIST.size, sdf, 0, mutableListOf<MemberJSON>(), ""))
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutsArrayPretty: String = gsonPretty.toJson(MEETING_LIST)
        writeTOFile(jsonTutsArrayPretty)
        addMembers(members)
    }

    fun addMember(member: Member) {
        if (MEETING_LIST.size != 0) {
            val m = MemberJSON(member.getName(), member.getStatus(), member.getSumm())
            MEETING_LIST[MEETING_LIST.size - 1].arrayMembers.add(m)
            MEETING_LIST[MEETING_LIST.size - 1].countMembers++
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val jsonTutsArrayPretty: String = gsonPretty.toJson(MEETING_LIST)
            writeTOFile(jsonTutsArrayPretty)
        }
        else {
            val arrayM: MutableList<Member> = mutableListOf()
            arrayM.add(member)
            addMeeting(arrayM)
        }
    }

    fun addMembers(members: MutableList<Member>) {
        if (members.size != 0 && MEETING_LIST.size != 0) {
            for (member: Member in members) {
                val m = MemberJSON(member.getName(), member.getStatus(), member.getSumm())
                MEETING_LIST[MEETING_LIST.size-1].arrayMembers.add(m)
                MEETING_LIST[MEETING_LIST.size-1].countMembers++
            }
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val jsonTutsArrayPretty: String = gsonPretty.toJson(MEETING_LIST)
            writeTOFile(jsonTutsArrayPretty)
        } else if (members.size != 0){
            addMeeting(members)
        }
    }

    fun addStringOutput(str: String){
        MEETING_LIST[MEETING_LIST.size-1].stringOutput = str
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutsArrayPretty: String = gsonPretty.toJson(MEETING_LIST)
        writeTOFile(jsonTutsArrayPretty)
    }


    //---- GETTERS
    fun getMeeting(id: Int): Meeting{
        if (id < MEETING_LIST.size)
            return MEETING_LIST[id]
        return Meeting(-1,"",0, mutableListOf(),"")
    }

    fun getAllMeetings(): MutableList<Meeting>{
        var meetings: MutableList<Meeting> = mutableListOf()
        val jsonStr: String = FileInputStream(FILE).bufferedReader().use { it.readText() }
        if (jsonStr.isNotEmpty()) {
            try {
                meetings = _gson.fromJson(jsonStr, mutableListTutorialType)
            } catch (e: Exception) {
                println("---- SMALL ERROR")
            }
        }
        return meetings
    }

    fun getCountMembers(): Int {
        if (MEETING_LIST.size != 0)
            return MEETING_LIST[MEETING_LIST.size-1].countMembers
        return 0
    }

    fun getMember(id: Int): Member {
        val member: Member = Member()
        member.setName(MEETING_LIST[MEETING_LIST.size-1].arrayMembers[id].name)
        member.setStatus(MEETING_LIST[MEETING_LIST.size-1].arrayMembers[id].status)
        member.setSumm(MEETING_LIST[MEETING_LIST.size-1].arrayMembers[id].summ)
        return member
    }

    fun getMember(name: String): Member? {
        var id = -1
        var count = 1
        for (member: MemberJSON in MEETING_LIST[MEETING_LIST.size-1].arrayMembers) {
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
        for (i: Int in 0 until getCountMembers()) {
            val member: Member = Member()
            member.setName(MEETING_LIST[MEETING_LIST.size-1].arrayMembers[i].name)
            member.setStatus(MEETING_LIST[MEETING_LIST.size-1].arrayMembers[i].status)
            member.setSumm(MEETING_LIST[MEETING_LIST.size-1].arrayMembers[i].summ)
            listMembers.add(member)
        }
        return listMembers
    }


    //OTHERS FUNCTIONS

    fun stringData(): String{
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
        } else {

        }


        val calendar = Calendar.getInstance()
        var strData = ""

        val day: String = calendar.get(Calendar.DAY_OF_MONTH).toString() + "-ое"
        var month: String = calendar.get(Calendar.MONTH).toString()
        var dayWeek: String = calendar.get(Calendar.DAY_OF_WEEK).toString()
        val year: String = calendar.get(Calendar.YEAR).toString()
        var hour: String = calendar.get(Calendar.HOUR).toString()
        var minute: String = calendar.get(Calendar.MINUTE).toString()
        var second: String = calendar.get(Calendar.SECOND).toString()

        month = convertMonthToSting(month.toInt())
        dayWeek = "(${convertDayWeekToString(dayWeek.toInt())})"

        hour = convertTimeToString(hour.toInt() + 12)
        minute = convertTimeToString(minute.toInt())
        second = convertTimeToString(second.toInt())

        strData = "$day ${month.toUpperCase()} $dayWeek\n $hour:$minute:$second $year"
        return strData

    }

    fun convertMonthToSting(month: Int): String{
        return when (month) {
            1 -> "ЯНВАРЯ"
            2 -> "ФЕВРАЛЯ"
            3 -> "МАРТА"
            4 -> "АПРЕЛЯ"
            5 -> "МАЯ"
            6 -> "ИЮНЯ"
            7 -> "ИЮЛЯ"
            8 -> "АВГУСТА"
            9 -> "СЕНТЯБРЯ"
            10 -> "ОКТЯБРЯ"
            11 -> "НОЯБРЯ"
            else -> "ДЕКАБРЯ"
        }
    }

    fun convertDayWeekToString(dayWeek: Int): String{
        return when (dayWeek) {
            2 -> "понедельник"
            3 -> "вторник"
            4 -> "среда"
            5 -> "четверг"
            6 -> "пятница"
            7 -> "суббота"
            else -> "воскресенье"
        }
    }

    fun convertTimeToString(number: Int): String {
        if (number < 10)
            return "0$number"
        return number.toString()
    }

    fun memberIsExist(member: Member): Boolean {
        for (m: MemberJSON in MEETING_LIST[MEETING_LIST.size-1].arrayMembers) {
            if (m.name == member.getName() && m.status == member.getStatus() && m.summ == member.getSumm())
                return true
        }
        return false
    }

    fun cleanDB() {
        writeTOFile("")
        MEETING_LIST = mutableListOf<Meeting>()
        MEMBER_LIST = mutableListOf<MemberJSON>()
        //addMeeting(mutableListOf())
    }

    fun stringAllUsers(): String {
        var str: String = ""
        for (m in MEMBER_LIST) {
            str += m.name + ":  " + m.status + "  " + m.summ + "\n"
        }
        return str
    }

    fun membersToMembersJSON(members: MutableList<Member>): MutableList<MemberJSON>{
        val membersJSON: MutableList<MemberJSON> = mutableListOf()
        for (member in members){
            membersJSON.add(MemberJSON(member.getName(), member.getStatus(), member.getSumm()))
        }
        return membersJSON
    }

    fun membersJSONToMembers(membersJSON: MutableList<MemberJSON>): MutableList<Member>{
        val members: MutableList<Member> = mutableListOf()
        for (memberJSON in membersJSON){
            members.add(Member(memberJSON.name, memberJSON.status))
        }
        return members
    }
}

class ProductsJSON {

    constructor(){}

    fun convertProductToJson(arrayProducts: MutableList<Product>): String{
        if (arrayProducts.size == 0)
            return ""

        val arrayProductsJSON: MutableList<DataBaseHandler.ProductJSON> = mutableListOf()
        for (product in arrayProducts){
            val prdctJSON = DataBaseHandler.ProductJSON(product.getName(), product.getPrice(), product.getCount().toFloat(), product.getSumm())
            arrayProductsJSON.add(prdctJSON)
        }
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        return gsonPretty.toJson(arrayProductsJSON)
    }

    fun convertProductToArray(json: String): MutableList<Product>{
        if (json.isEmpty())
            return mutableListOf()

        val gson = Gson()
        val mutableListTutorialTypeProduct = object : TypeToken<MutableList<DataBaseHandler.ProductJSON>>() {}.type
        val arrayProductsJSON: MutableList<DataBaseHandler.ProductJSON> = gson.fromJson(json, mutableListTutorialTypeProduct)

        val arrayProducts: MutableList<Product> = mutableListOf()
        for (productJSON in arrayProductsJSON){
            arrayProducts.add(Product(productJSON.name, productJSON.price, productJSON.count.toFloat()))
        }
        return arrayProducts
    }
}