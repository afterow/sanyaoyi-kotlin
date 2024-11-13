package com.afterow.sanyaoyi.utils

import com.tyme.eightchar.EightChar
import com.tyme.lunar.LunarHour
import com.tyme.solar.SolarTime

fun mainx() {

    val tianganquence = listOf("0", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    val dizhiquence =
        listOf("0", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

    val indexOfBing = tianganquence.indexOf("丙") // 这将返回3
    val indexOfBing2 = dizhiquence.indexOf("子") // 这将返回1

    /**
     * 三才数包括:天成数、人成数、地成数。这是《易数玄机》独创的法则,只在依据问卜者生辰排演三才时有用。
     * 天成数包括年和月,地成数包括月和时。人成数的基数为五。
     * 例:天成数:丙子年生,丙数为三,壬午年生,壬数为九,月数,十二月就是十二数,年数加月数,以八除余数定上卦;
     * 人成数:基数为五,男加一,女加二,再加生肖序数,用八除得余数定中卦。
     * 地成数:每月三十天,地数三十,古代以地支计时,一昼夜十二时辰,也列为地成数,日数加时辰以八除余数定下卦。
     * 按天地人三类数字排卦,故为三才数。
     */


    fun getLunarData(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
        gender: String
    ): Triple<EightChar, LunarHour, List<Any>> {

        // 该函数根据输入的日期和时间计算对应的八字（Eight Characters）和农历（Lunar Hour）。
        val solarTime = SolarTime.fromYmdHms(year, month, day, hour, minute, second)
        val eightChar = solarTime.lunarHour.eightChar
        val lunarHour = solarTime.getLunarHour()
        val xinmingshu = if (gender == "男") 1 else 2


        val nianshu = tianganquence.indexOf(eightChar.toString()[0].toString())
        val yushu = dizhiquence.indexOf(eightChar.toString()[4].toString()) - 2
        val shuxiang = dizhiquence.indexOf(eightChar.toString()[1].toString())
        val rishu = lunarHour.day
        val shishu = dizhiquence.indexOf(eightChar.toString()[10].toString())


        val tianchengshu = (nianshu + yushu) % 8
        val renchengshu = (5 + xinmingshu + shuxiang) % 8
        val dichengshu = (rishu + shishu) % 8
        val bagualist = listOf(tianchengshu, renchengshu, dichengshu)

        val x1 =bagualist[0]
        val x2 =bagualist[1]
        val x3 =bagualist[2]
        val processor = GuaCalculator()
        val results = processor.toggleElement(listOf(x1, x2, x3))

        // 己卯 辛未 癸酉 庚申
        // 农历己卯年六月十九庚申时
        // [蒙, 复, 颐, 屯, 剥, 比, 豫, 蹇, 小过, 谦, 解, 师, 坤, 艮, 震, 坎]
        return Triple(eightChar, lunarHour, results)
    }

    val (eightChar, lunarHour ,results) = getLunarData(1939, 8, 4, 16, 37, 0, "男")
    println(eightChar)
    println(lunarHour)
    println(results)



}





