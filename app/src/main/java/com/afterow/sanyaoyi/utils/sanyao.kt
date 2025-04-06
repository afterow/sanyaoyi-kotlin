package com.afterow.sanyaoyi.utils

class GuaCalculator {

    private val bagua = mapOf(
        "乾" to listOf(listOf(1, 1, 1), 1),
        "兑" to listOf(listOf(0, 1, 1), 2),
        "离" to listOf(listOf(1, 0, 1), 3),
        "震" to listOf(listOf(0, 0, 1), 4),
        "巽" to listOf(listOf(1, 1, 0), 5),
        "坎" to listOf(listOf(0, 1, 0), 6),
        "艮" to listOf(listOf(1, 0, 0), 7),
        "坤" to listOf(listOf(0, 0, 0), 8)
    )

    private val biggua = arrayListOf(
        listOf("乾为天", "天泽履", "天火同人", "天雷无妄", "天风姤", "天水讼", "天山遁", "天地否"),
        listOf("泽天夬", "兑为泽", "泽火革", "泽雷随", "泽风大过", "泽水困", "泽山咸", "泽地萃"),
        listOf("火天大有", "火泽睽", "离为火", "火雷噬嗑", "火风鼎", "火水未济", "火山旅", "火地晋"),
        listOf("雷天大壮", "雷泽归妹", "雷火丰", "震为雷", "雷风恒", "雷水解", "雷山小过", "雷地豫"),
        listOf("风天小畜", "风泽中孚", "风火家人", "风雷益", "巽为风", "风水涣", "风山渐", "风地观"),
        listOf("水天需", "水泽节", "水火既济", "水雷屯", "水风井", "坎为水", "水山蹇", "水地比"),
        listOf("山天大蓄", "山泽损", "山火贲", "山雷颐", "山风蛊", "山水蒙", "艮为山", "山地剥"),
        listOf("地天泰", "地泽临", "地火明夷", "地雷复", "地风升", "地水师", "地山谦", "坤为地")
    )

    private fun indexBigGua(shanggua: Int, xiagua: Int): String {
        return biggua[shanggua - 1][xiagua - 1]
    }

    private fun findKeyByValue(targetValue: List<Int>): List<Any>? {
        for ((key, value) in bagua) {
            if (value[0] == targetValue) {
                return listOf(key, value[0], value[1])
            }
        }
        return null
    }

    private fun applyYaoChanges(original: List<Int>, yaobianList: List<Int>): List<Int> {
        return original.mapIndexed { index, value ->
            if (yaobianList.contains(index)) {
                if (value % 2 == 0) 1 else 0
            } else {
                if (value % 2 == 0) 0 else 1
            }
        }
    }

    fun toggleElement(targetValue: List<Int>, yaobianList: List<Int>): List<String> {
        // 确保输入是6爻
        if (targetValue.size != 6) {
            return listOf("请输入6个爻")
        }

        // 计算本卦和变卦
        val benGuaElements = targetValue.map { if (it % 2 == 0) 0 else 1 }
        val bianGuaElements = applyYaoChanges(targetValue, yaobianList)

        // 获取上下卦
        val shangBen = benGuaElements.subList(0, 3)
        val xiaBen = benGuaElements.subList(3, 6)
        val shangBian = bianGuaElements.subList(0, 3)
        val xiaBian = bianGuaElements.subList(3, 6)

        // 获取卦信息
        val benShang = findKeyByValue(shangBen) ?: return listOf("本卦上卦未找到")
        val benXia = findKeyByValue(xiaBen) ?: return listOf("本卦下卦未找到")
        val bianShang = findKeyByValue(shangBian) ?: return listOf("变卦上卦未找到")
        val bianXia = findKeyByValue(xiaBian) ?: return listOf("变卦下卦未找到")

        // 计算互卦（取2-4爻和3-5爻）
        val huBenElements = benGuaElements.subList(1, 4) + benGuaElements.subList(2, 5)
        val huBenShang = huBenElements.subList(0, 3)
        val huBenXia = huBenElements.subList(3, 6)
        
        // 互卦的变卦也要应用动爻变化
        val huBianElements = bianGuaElements.subList(1, 4) + bianGuaElements.subList(2, 5)
        val huBianShang = huBianElements.subList(0, 3)
        val huBianXia = huBianElements.subList(3, 6)

        // 获取互卦信息
        val huBenShangGua = findKeyByValue(huBenShang) ?: return listOf("互本上卦未找到")
        val huBenXiaGua = findKeyByValue(huBenXia) ?: return listOf("互本下卦未找到")
        val huBianShangGua = findKeyByValue(huBianShang) ?: return listOf("互变上卦未找到")
        val huBianXiaGua = findKeyByValue(huBianXia) ?: return listOf("互变下卦未找到")

        // 获取卦序号
        val ixBenShang = benShang[2] as Int
        val ixBenXia = benXia[2] as Int
        val ixBianShang = bianShang[2] as Int
        val ixBianXia = bianXia[2] as Int
        val ixHuBenShang = huBenShangGua[2] as Int
        val ixHuBenXia = huBenXiaGua[2] as Int
        val ixHuBianShang = huBianShangGua[2] as Int
        val ixHuBianXia = huBianXiaGua[2] as Int

        fun takeLastBasedOnLength(input: String): String {
            return if (input.length >= 4) input.takeLast(2) else input.takeLast(1)
        }

        // 计算各宫
        val ziGong = takeLastBasedOnLength(indexBigGua(ixBianShang, ixHuBianXia))
        val chouGong = takeLastBasedOnLength(indexBigGua(ixBenShang, ixHuBenXia))
        val yinGong = takeLastBasedOnLength(indexBigGua(ixBianShang, ixHuBenXia))
        val maoGong = takeLastBasedOnLength(indexBigGua(ixHuBianShang, ixHuBenXia))

        val chenGong = takeLastBasedOnLength(indexBigGua(ixBianShang, ixBenXia))
        val siGong = takeLastBasedOnLength(indexBigGua(ixHuBianShang, ixBenXia))
        val wuGong = takeLastBasedOnLength(indexBigGua(ixHuBenShang, ixBenXia))
        val weiGong = takeLastBasedOnLength(indexBigGua(ixHuBianShang, ixBianXia))

        val shenGong = takeLastBasedOnLength(indexBigGua(ixHuBenShang, ixBianXia))
        val youGong = takeLastBasedOnLength(indexBigGua(ixBenShang, ixBianXia))
        val xuGong = takeLastBasedOnLength(indexBigGua(ixHuBenShang, ixHuBianXia))
        val haiGong = takeLastBasedOnLength(indexBigGua(ixBenShang, ixHuBianXia))

        return listOf(
            ziGong,
            chouGong,
            yinGong,
            maoGong,
            chenGong,
            siGong,
            wuGong,
            weiGong,
            shenGong,
            youGong,
            xuGong,
            haiGong,
            benShang[0] as String + benXia[0] as String,
            bianShang[0] as String + bianXia[0] as String,
            huBenShangGua[0] as String + huBenXiaGua[0] as String,
            huBianShangGua[0] as String + huBianXiaGua[0] as String
        )
    }
}

fun main() {
    val processor = GuaCalculator()
    val results = processor.toggleElement(listOf(9, 8, 7, 6, 5, 4), listOf(0, 2))
    println(results)
}
