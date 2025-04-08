package com.afterow.sanyaoyi.utils

class GuaCalculator {

    // region 八卦基础数据
    // 八卦对应二进制(阳爻1,阴爻0)和序号（从下往上排列）
    // 格式：卦名 -> [二进制列表(下爻,中爻,上爻), 序号]
    private val bagua: Map<String, List<Any>> = mapOf(
        "乾" to listOf(listOf(1, 1, 1), 1), // 三个阳爻
        "兑" to listOf(listOf(0, 1, 1), 2), // 下阴中阳上阳
        "离" to listOf(listOf(1, 0, 1), 3), // 下阳中阴上阳
        "震" to listOf(listOf(0, 0, 1), 4), // 下中阴，上阳
        "巽" to listOf(listOf(1, 1, 0), 5), // 下中阳，上阴
        "坎" to listOf(listOf(0, 1, 0), 6), // 下阴中阳上阴
        "艮" to listOf(listOf(1, 0, 0), 7), // 下阳中阴上阴
        "坤" to listOf(listOf(0, 0, 0), 8)  // 三个阴爻
    )

    // 64卦排列（8x8矩阵），行代表上卦，列代表下卦
    // 格式：biggua[上卦序号-1][下卦序号-1] = 卦名
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
    // endregion

    /**
     * 根据上下卦序号获取完整卦名
     * @param shanggua 上卦序号（1-8）
     * @param xiagua 下卦序号（1-8）
     * @return 组合后的卦名，如"乾为天"
     */
    private fun indexBigGua(shanggua: Int, xiagua: Int): String {
        return biggua[shanggua - 1][xiagua - 1] // 矩阵索引从0开始
    }

    /**
     * 根据二进制值查找对应的八卦信息
     * @param targetValue 目标二进制值（3位，从下到上）
     * @return 包含卦名、二进制值、序号的列表，找不到返回null
     */
    private fun findKeyByValue(targetValue: List<Int>): List<Any>? {
        for ((key, value) in bagua) {
            if (value[0] == targetValue) {
                return listOf(key, value[0], value[1])
            }
        }
        return null
    }

    // region 核心计算逻辑
    /**
     * 计算卦象变化并生成十二宫和四卦结果
     * @param targetValue 初始爻值列表（3位，数值奇偶决定阴阳）
     * @param yaobianList 动爻位置列表（从0开始的索引，0表示最下爻）
     * @return 包含12宫名称和4个卦名的列表（顺序：子-亥宫 + 本卦+变卦+互本+互变）
     */
    fun toggleElement(targetValue: List<Int>, yaobianList: List<Int>): List<String> {
        // 将初始爻值转换为二进制（本卦）
        // 奇数为阳（1），偶数为阴（0），顺序从下到上
        val originalBinary: MutableList<Int> = targetValue.map { if (it % 2 == 0) 0 else 1 }.toMutableList()

        // region 计算变卦（应用动爻变化）
        val tmpList: MutableList<Int> = originalBinary.toMutableList()
        yaobianList.forEach { yaobian ->
            // 动爻位置从下往上（0=最下爻），翻转阴阳
            tmpList[yaobian] = if (tmpList[yaobian] == 1) 0 else 1
        }
        val bianGua: List<Any> = findKeyByValue(tmpList) ?: return listOf("变卦未找到")
        // endregion

        // region 获取本卦信息
        val manGua: List<Any> = findKeyByValue(originalBinary) ?: return listOf("本卦未找到")
        // endregion

        // region 计算互卦（本卦和变卦的中间爻组合）
        // 互本卦：取本卦的2-4爻（索引1,2）和变卦的中间爻（索引1）
        val huBenElements: List<Int> = listOf(originalBinary[1], originalBinary[2], tmpList[0])
        val huBen: List<Any> = findKeyByValue(huBenElements) ?: return listOf("互本未找到")

        // 互变卦：在互本卦基础上应用相同动爻变化
        val huBianElements: MutableList<Int> = huBenElements.toMutableList()
        // region 计算变卦（应用动爻变化）
        yaobianList.forEach { yaobian ->
            // 确保动爻位置在0-2范围内（互卦只有3爻）
            if (yaobian in 0..2) {
                huBianElements[yaobian] = if (huBianElements[yaobian] == 1) 0 else 1
            }
        }
        val huBian: List<Any> = findKeyByValue(huBianElements) ?: return listOf("互变未找到")
        // endregion

        // 获取各卦的序号（用于后续组合计算）
        val ixman: Int = manGua[2] as Int    // 本卦序号
        val ixbianGua: Int = bianGua[2] as Int // 变卦序号
        val ixhuBen: Int = huBen[2] as Int   // 互本卦序号
        val ixhuBian: Int = huBian[2] as Int // 互变卦序号

        // region 生成十二宫名称（通过不同卦序组合）
        // 处理卦名后缀（取卦名最后1-2个字）
        fun takeLastBasedOnLength(input: String): String {
            return if (input.length >= 4) input.takeLast(2) else input.takeLast(1)
        }

        // 十二宫计算规则（通过不同卦序组合生成）
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
        // endregion

        return listOf(
            ziGong, chouGong, yinGong, maoGong,
            chenGong, siGong, wuGong, weiGong,
            shenGong, youGong, xuGong, haiGong,
            manGua[0] as String,   // 本卦名称
            bianGua[0] as String,  // 变卦名称
            huBen[0] as String,    // 互本卦名称
            huBian[0] as String     // 互变卦名称
        )
    }
    // endregion
}

fun main() {
    val calculator = GuaCalculator()
    // 测试用例：初始爻值[0（阴）, 8（偶数为阴）, 0（阴）]，动爻位置0（最下爻）
    val results = calculator.toggleElement(listOf(0, 8, 0), listOf(0))
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