package com.afterow.sanyaoyi.utils


class GuaCalculator2 {

    // 八卦基础数据
    private val bagua: Map<String, List<Any>> = mapOf(
        "乾" to listOf(listOf(1, 1, 1), 1),
        "兑" to listOf(listOf(0, 1, 1), 2),
        "离" to listOf(listOf(1, 0, 1), 3),
        "震" to listOf(listOf(0, 0, 1), 4),
        "巽" to listOf(listOf(1, 1, 0), 5),
        "坎" to listOf(listOf(0, 1, 0), 6),
        "艮" to listOf(listOf(1, 0, 0), 7),
        "坤" to listOf(listOf(0, 0, 0), 8)
    )

    // 64卦排列（8x8矩阵）
    private val bigGua = listOf(
        listOf("乾为天", "天泽履", "天火同人", "天雷无妄", "天风姤", "天水讼", "天山遁", "天地否"),
        listOf("泽天夬", "兑为泽", "泽火革", "泽雷随", "泽风大过", "泽水困", "泽山咸", "泽地萃"),
        listOf("火天大有", "火泽睽", "离为火", "火雷噬嗑", "火风鼎", "火水未济", "火山旅", "火地晋"),
        listOf("雷天大壮", "雷泽归妹", "雷火丰", "震为雷", "雷风恒", "雷水解", "雷山小过", "雷地豫"),
        listOf("风天小畜", "风泽中孚", "风火家人", "风雷益", "巽为风", "风水涣", "风山渐", "风地观"),
        listOf("水天需", "水泽节", "水火既济", "水雷屯", "水风井", "坎为水", "水山蹇", "水地比"),
        listOf("山天大蓄", "山泽损", "山火贲", "山雷颐", "山风蛊", "山水蒙", "艮为山", "山地剥"),
        listOf("地天泰", "地泽临", "地火明夷", "地雷复", "地风升", "地水师", "地山谦", "坤为地")
    )

    // 根据上下卦序号获取完整卦名
    private fun indexBigGua(shanggua: Int, xiagua: Int): String {
        return bigGua[shanggua - 1][xiagua - 1]
    }

    // 获取卦名的最后一个字
    private fun takeLastBasedOnLength(input: String): String {
        return if (input.length >= 4) input.takeLast(2) else input.takeLast(1)
    }

    // 根据二进制值查找对应的八卦信息
    private fun findKeyByValue(targetValue: List<Int>): List<Any>? {
        for ((key, value) in bagua) {
            if (value[0] == targetValue) {
                return listOf(key, value[0], value[1])
            }
        }
        return null
    }

    // 核心计算函数 - 添加动爻列表参数
    fun calculateGua(numsList: List<Int>, yaobianList: List<Int> = emptyList()): List<String> {
        // 1. 计算奇偶性（0为偶，1为奇）
        val parityList = numsList.map { if (it % 2 == 0) 0 else 1 }

        // 2. 找到本卦信息
        val benGua = findKeyByValue(parityList) ?: return listOf("未找到对应的卦")

        // 3. 提取本卦的二进制值
        val targetValue = benGua[1] as List<Int>

        // 4. 计算变卦 - 支持动爻列表
        val tmpList = targetValue.toMutableList()

        // 如果有传入动爻列表，使用动爻列表
        if (yaobianList.isNotEmpty()) {
            yaobianList.forEach { yaobian ->
                if (yaobian in 0..2) {
                    tmpList[yaobian] = if (tmpList[yaobian] == 1) 0 else 1
                }
            }
        }
        // 如果没有传入动爻列表，使用原来的模3方法
        else {
            val totalSum = tmpList.sum()
            val yaobian = totalSum % 3
            tmpList[yaobian] = if (tmpList[yaobian] == 1) 0 else 1
        }

        val bianGua = findKeyByValue(tmpList) ?: return listOf("未找到变卦")

        // 5. 计算互卦
        val flattenedList = targetValue + tmpList
        val huBen = findKeyByValue(flattenedList.subList(1, 4)) ?: return listOf("未找到互本卦")
        val huBian = findKeyByValue(flattenedList.subList(2, 5)) ?: return listOf("未找到互变卦")

        // 6. 获取各卦的序号
        val ixman = benGua[2] as Int
        val ixbianGua = bianGua[2] as Int
        val ixhuBen = huBen[2] as Int
        val ixhuBian = huBian[2] as Int

        // 7. 计算十二宫
        val ziGong = takeLastBasedOnLength(indexBigGua(ixbianGua, ixhuBian))
        val chouGong = takeLastBasedOnLength(indexBigGua(ixman, ixhuBen))
        val yinGong = takeLastBasedOnLength(indexBigGua(ixbianGua, ixhuBen))
        val maoGong = takeLastBasedOnLength(indexBigGua(ixhuBian, ixhuBen))
        val chenGong = takeLastBasedOnLength(indexBigGua(ixbianGua, ixman))
        val siGong = takeLastBasedOnLength(indexBigGua(ixhuBian, ixman))
        val wuGong = takeLastBasedOnLength(indexBigGua(ixhuBen, ixman))
        val weiGong = takeLastBasedOnLength(indexBigGua(ixhuBian, ixbianGua))
        val shenGong = takeLastBasedOnLength(indexBigGua(ixhuBen, ixbianGua))
        val youGong = takeLastBasedOnLength(indexBigGua(ixman, ixbianGua))
        val xuGong = takeLastBasedOnLength(indexBigGua(ixhuBen, ixhuBian))
        val haiGong = takeLastBasedOnLength(indexBigGua(ixman, ixhuBian))

        // 8. 返回结果列表
        return listOf(
            ziGong, chouGong, yinGong, maoGong,
            chenGong, siGong, wuGong, weiGong,
            shenGong, youGong, xuGong, haiGong,
            benGua[0] as String,   // 本卦名称
            bianGua[0] as String,  // 变卦名称
            huBen[0] as String,     // 互本卦名称
            huBian[0] as String     // 互变卦名称
        )
    }
}

fun main() {
    val calculator = GuaCalculator2()

    // 示例1：使用默认动爻计算（模3方法）
    println("===== 使用默认动爻计算 =====")
    var results = calculator.calculateGua(listOf(0, 1, 0))
    printResults(results)

    // 示例2：使用自定义动爻列表
    println("\n===== 使用自定义动爻列表 =====")
    results = calculator.calculateGua(listOf(0, 1, 0), listOf(1))
    printResults(results)
}

fun printResults(results: List<String>) {
    if (results.size < 16) {
        println("计算错误: ${results.firstOrNull() ?: "未知错误"}")
        return
    }

    // 输出十二宫
    println("===== 十二宫 =====")
    val palaces = listOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    palaces.forEachIndexed { index, palace ->
        println("${palace}宫：${results[index]}")
    }

    // 输出卦象信息
    println("\n===== 卦象信息 =====")
    println("本卦：${results[12]}")
    println("变卦：${results[13]}")
    println("互本卦：${results[14]}")
    println("互变卦：${results[15]}")
}