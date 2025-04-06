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

    fun toggleElement(targetValue: List<Int>, yaobianList: List<Int>): List<String> {
        val tmpList = targetValue.map { if (it % 2 == 0) 0 else 1 }.toMutableList()

        yaobianList.forEach { yaobian ->
            tmpList[yaobian] = if (tmpList[yaobian] == 1) 0 else 1
        }

        val bianGua = findKeyByValue(tmpList) ?: return listOf("变卦未找到")
        val flattenedList = targetValue + tmpList

        val manGua = findKeyByValue(targetValue.map { if (it % 2 == 0) 0 else 1 }) ?: return listOf("本卦未找到")

        val huBenElements = flattenedList.subList(1, 4).map { if (it % 2 == 0) 0 else 1 }
        val huBen = findKeyByValue(huBenElements) ?: return listOf("互本未找到")

        val huBianElements = flattenedList.subList(2, 5).map { if (it % 2 == 0) 0 else 1 }
        val huBian = findKeyByValue(huBianElements) ?: return listOf("互变未找到")

        val ixman = manGua[2] as Int
        val ixbianGua = bianGua[2] as Int
        val ixhuBen = huBen[2] as Int
        val ixhuBian = huBian[2] as Int

        fun takeLastBasedOnLength(input: String): String {
            return if (input.length >= 4) input.takeLast(2) else input.takeLast(1)
        }

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
            manGua[0] as String,
            bianGua[0] as String,
            huBen[0] as String,
            huBian[0] as String
        )
    }
}

fun main() {
    val processor = GuaCalculator()
    val results = processor.toggleElement(listOf(9, 8, 0), listOf(0, 2))
    println(results)
}