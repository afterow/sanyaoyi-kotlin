package com.afterow.sanyaoyi.utils

import com.tyme.eightchar.EightChar
import com.tyme.lunar.LunarHour
import com.tyme.solar.SolarTime
import java.time.LocalDateTime

object LunarUtils {

    private val tianganquence = listOf("0", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    private val dizhiquence = listOf("0", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

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
        println(bagualist)
        val dynamicIndex = (tianchengshu + renchengshu + dichengshu) % 3

        val processor = GuaCalculator()
        val results = processor.toggleElement(bagualist, listOf(dynamicIndex)) // 传入动爻索引列表

        return Triple(eightChar, lunarHour, results)
    }
}

fun main() {
    val currentDateTime = LocalDateTime.now()
    val (eightChar, lunarHour, results) = LunarUtils.getLunarData(
        currentDateTime.year,
        currentDateTime.monthValue,
        currentDateTime.dayOfMonth,
        currentDateTime.hour,
        currentDateTime.minute,
        currentDateTime.second,
        "男"
    )
    println("八字: $eightChar")
    println("农历时辰: $lunarHour")
    println("三才数: $results")
}
