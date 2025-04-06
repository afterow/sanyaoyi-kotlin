package com.afterow.sanyaoyi.utils

class GuaCalculator {

    // 八卦对应二进制和序号
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

    // 64卦排列
    private val biggua: List<List<String>> = arrayListOf(
        listOf("乾为天", "天泽履", "天火同人", "天雷无妄", "天风姤", "天水讼", "天山遁", "天地否"),
        listOf("泽天夬", "兑为泽", "泽火革", "泽雷随", "泽风大过", "泽水困", "泽山咸", "泽地萃"),
        listOf("火天大有", "火泽睽", "离为火", "火雷噬嗑", "火风鼎", "火水未济", "火山旅", "火地晋"),
        listOf("雷天大壮", "雷泽归妹", "雷火丰", "震为雷", "雷风恒", "雷水解", "雷山小过", "雷地豫"),
        listOf("风天小畜", "风泽中孚", "风火家人", "风雷益", "巽为风", "风水涣", "风山渐", "风地观"),
        listOf("水天需", "水泽节", "水火既济", "水雷屯", "水风井", "坎为水", "水山蹇", "水地比"),
        listOf("山天大蓄", "山泽损", "山火贲", "山雷颐", "山风蛊", "山水蒙", "艮为山", "山地剥"),
        listOf("地天泰", "地泽临", "地火明夷", "地雷复", "地风升", "地水师", "地山谦", "坤为地")
    )

    // 根据上下卦序号获取卦名
    private fun indexBigGua(shanggua: Int, xiagua: Int): String {
        return biggua[shanggua - 1][xiagua - 1]
    }

    // 根据二进制值查找卦名
    private fun findKeyByValue(targetValue: List<Int>): List<Any>? {
        for ((key, value) in bagua) {
            if (value[0] == targetValue) {
                return listOf(key, value[0], value[1])
            }
        }
        return null
    }

    // 核心计算逻辑
    fun toggleElement(targetValue: List<Int>, yaobianList: List<Int>): List<String> {
        // 原始卦转换为二进制（本卦）
        val originalBinary: MutableList<Int> = targetValue.map { if (it % 2 == 0) 0 else 1 }.toMutableList()
        
        // 应用动爻变化得到变卦
        val tmpList: MutableList<Int> = originalBinary.toMutableList()
        yaobianList.forEach { yaobian ->
            tmpList[yaobian] = if (tmpList[yaobian] == 1) 0 else 1
        }
        val bianGua: List<Any> = findKeyByValue(tmpList) ?: return listOf("变卦未找到")

        // 获取本卦信息
        val manGua: List<Any> = findKeyByValue(originalBinary) ?: return listOf("本卦未找到")

        // 计算互本卦（2-4爻）
        val huBenElements: List<Int> = listOf(originalBinary[1], originalBinary[2], tmpList[1]) // 2-4爻
        val huBen: List<Any> = findKeyByValue(huBenElements) ?: return listOf("互本未找到")

        // 计算互变卦（在互本卦基础上应用相同动爻变化）
        val huBianElements: MutableList<Int> = huBenElements.toMutableList()
        yaobianList.forEach { yaobian ->
            // 只变动互卦中的爻，yaobian从1开始，转为0索引
            if (yaobian in 1..3) {
                huBianElements[yaobian - 1] = if (huBianElements[yaobian - 1] == 1) 0 else 1
            }
        }
        val huBian: List<Any> = findKeyByValue(huBianElements) ?: return listOf("互变未找到")

        // 获取各卦序号
        val ixman: Int = manGua[2] as Int
        val ixbianGua: Int = bianGua[2] as Int
        val ixhuBen: Int = huBen[2] as Int
        val ixhuBian: Int = huBian[2] as Int

        // 处理卦名后缀
        fun takeLastBasedOnLength(input: String): String {
            return if (input.length >= 4) input.takeLast(2) else input.takeLast(1)
        }

        // 计算十二宫
        val ziGong: String = takeLastBasedOnLength(indexBigGua(ixbianGua, ixhuBian))
        val chouGong: String = takeLastBasedOnLength(indexBigGua(ixman, ixhuBen))
        val yinGong: String = takeLastBasedOnLength(indexBigGua(ixbianGua, ixhuBen))
        val maoGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixhuBen))

        val chenGong: String = takeLastBasedOnLength(indexBigGua(ixbianGua, ixman))
        val siGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixman))
        val wuGong: String = takeLastBasedOnLength(indexBigGua(ixhuBen, ixman))
        val weiGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixbianGua))

        val shenGong: String = takeLastBasedOnLength(indexBigGua(ixhuBen, ixbianGua))
        val youGong: String = takeLastBasedOnLength(indexBigGua(ixman, ixbianGua))
        val xuGong: String = takeLastBasedOnLength(indexBigGua(ixhuBen, ixhuBian))
        val haiGong: String = takeLastBasedOnLength(indexBigGua(ixman, ixhuBian))

        return listOf(
            ziGong, chouGong, yinGong, maoGong,
            chenGong, siGong, wuGong, weiGong,
            shenGong, youGong, xuGong, haiGong,
            manGua[0] as String,   // 本卦
            bianGua[0] as String,  // 变卦
            huBen[0] as String,    // 互本
            huBian[0] as String     // 互变
        )
    }
}

fun main() {
    val calculator = GuaCalculator()
    // 测试用例：初始爻值[0, 8, 0]，动爻位置0
    val results = calculator.toggleElement(listOf(0, 8, 0), listOf(1)) // 测试动爻位置为1
    println("十二宫+四卦结果：")
    results.forEachIndexed { index, value ->
        when (index) {
            in 0..11 -> println("${"子丑寅卯辰巳午未申酉戌亥"[index]}宫：$value")
            12 -> println("\n本卦：$value")
            13 -> println("变卦：$value")
            14 -> println("互本：$value")
            15 -> println("互变：$value")
        }
    }
}
