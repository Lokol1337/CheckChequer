package com.example.checkchequer

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val test:DataBaseHandler = DataBaseHandler("1234")
        test.cleanDB()
        val testMember:Member = Member()
        testMember.setName("testName")
        test.addMember(testMember)
        assertEquals(test.getMember(0).getName(),"testName")
    }
    @Test
    fun addition_isCorrect_2() {
        val test:DataBaseHandler = DataBaseHandler("1234")
        test.cleanDB()
        val testMember1:Member = Member()
        testMember1.setName("testName")
        val testMember2:Member = Member()
        testMember2.setName("testName")
        val testMember3:Member = Member()
        testMember3.setName("difTestName")
        test.addMember(testMember1)
        if(test.memberIsExist(testMember2) == true && test.memberIsExist(testMember3)==false)
            assertEquals(true,true)
        else
            assertEquals(true,false)

    }

    @Test
    fun addition_isCorrect_3() {
        val test:DataBaseHandler = DataBaseHandler("1234")
        test.cleanDB()
        val testMember1:Member = Member()
        testMember1.setName("testName1")
        val testMember2:Member = Member()
        testMember2.setName("testName2")
        val testMember3:Member = Member()
        testMember3.setName("testName3")
        val Members:MutableList<Member> = mutableListOf()
        Members.add(testMember1)
        Members.add(testMember2)
        Members.add(testMember3)
        test.addMembers(Members)
        if(test.getMember(0).getName() == "testName1" && test.getMember(1).getName() == "testName2" && test.getMember(2).getName() == "testName3")
            assertEquals(true,true)
        else
            assertEquals(true,false)
    }

    @Test
    fun addition_isCorrect_4() {
        val test:DataBaseHandler = DataBaseHandler("1234")
        test.cleanDB()
        val testMember1:Member = Member()
        testMember1.setName("testName1")
        val testMember2:Member = Member()
        testMember2.setName("testName2")
        val testMember3:Member = Member()
        testMember3.setName("testName3")
        val Members:MutableList<Member> = mutableListOf()
        Members.add(testMember1)
        Members.add(testMember2)
        Members.add(testMember3)
        test.addMembers(Members)
        assertEquals(3,test.getCountMembers())
    }

    @Test
    fun addition_isCorrect_5() {
        val test:DataBaseHandler = DataBaseHandler("1234")
        val testMember:Member = Member()
        testMember.setName("testName")
        test.addMember(testMember)
        test.cleanDB()
        assertEquals(0,test.getCountMembers())
    }
}
