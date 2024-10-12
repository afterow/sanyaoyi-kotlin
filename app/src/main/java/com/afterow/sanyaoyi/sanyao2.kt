package com.afterow.sanyaoyi

class GuaCalculator2 {

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
        listOf("山天大蓄", "山泽损", "山火贲", "山雷姬", "山风蛊", "山水蒙", "艮为山", "山地剥"),
        listOf("地天泰", "地泽临", "地火明夷", "地雷复", "地风升", "地水师", "地山谦", "坤为地")
    )

    private fun indexBigGua(shanggua: Int, xiagua: Int): String {
        return biggua[shanggua - 1][xiagua - 1]
    }

    private fun findKeyByValue(baguaDict: Map<String, List<Any>>, targetValue: Any): List<Any>? {
        for ((key, value) in baguaDict) {
            if (value[0] == targetValue) {
                return mutableListOf(key, value[0], value[1])
            }
        }
        // 返回 [乾, [1, 1, 1], 1]
        return null
    }

    fun toggleElement(targetValue: List<Int>): String {
        val tmpList = mutableListOf<Int>().apply { addAll(targetValue) }
        val manGua = findKeyByValue(bagua, tmpList) ?: return "本卦未找到"

        val totalSum = tmpList.sumOf { it }
        val yaobian = totalSum % 3
        tmpList[yaobian] = if (tmpList[yaobian] == 1) 0 else 1

        val flattenedList = mutableListOf(targetValue, tmpList).flatten()
        val bianGua = findKeyByValue(bagua, tmpList)
        val huBen = findKeyByValue(bagua, flattenedList.subList(1, 4))
        val huBian = findKeyByValue(bagua, flattenedList.subList(2, 5))

        val resultTmpList = flattenedList.map { if (it == 0) 1 else 0 }

        val cuoBen = findKeyByValue(bagua, resultTmpList.subList(0, 3))
        val cuoBian = findKeyByValue(bagua, resultTmpList.subList(3, 6))

        val reverseTmpList = flattenedList.reversed()
        val zongBen = findKeyByValue(bagua, reverseTmpList.subList(0, 3))
        val zongBian = findKeyByValue(bagua, reverseTmpList.subList(3, 6))


        val ixman = (manGua.getOrNull(2)as Int)
        val ixbianGua:Int =(bianGua?.getOrNull(2) as Int)
        val ixhuBen =(huBen?.getOrNull(2)as Int)
        val ixhuBian:Int = (huBian?.getOrNull(2) as Int)


        fun takeLastBasedOnLength(input: String): String {
            return if (input.length >= 4) {
                input.takeLast(2)
            } else {
                input.takeLast(1)
            }
        }

        val ziGong: String = takeLastBasedOnLength(indexBigGua(ixbianGua, ixhuBian))
        val chouGong: String = takeLastBasedOnLength(indexBigGua(ixman, ixhuBen))
        val yinGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixhuBen))
        val maoGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixhuBian))
        val chenGong: String = takeLastBasedOnLength(indexBigGua(ixman, ixbianGua))
        val siGong: String = takeLastBasedOnLength(indexBigGua(ixman, ixhuBian))
        val wuGong: String = takeLastBasedOnLength(indexBigGua(ixhuBen, ixman))
        val weiGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixbianGua))
        val shenGong: String = takeLastBasedOnLength(indexBigGua(ixhuBen, ixbianGua))
        val youGong: String = takeLastBasedOnLength(indexBigGua(ixbianGua, ixman))
        val xuGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixman))
        val haiGong: String = takeLastBasedOnLength(indexBigGua(ixhuBian, ixman))

        return  buildString {
            append(ziGong)
            append(chouGong)
            append(yinGong)
            append(maoGong)
            append(chenGong)
            append(siGong)
            append(wuGong)
            append(weiGong)
            append(shenGong)
            append(youGong)
            append(xuGong)
            append(haiGong)

            // 追加其他值
            append(manGua[0])
            append(bianGua.getOrNull(0) ?: "null") // 使用 ?: 提供默认值以防 getOrNull 返回 null
            append(huBen.getOrNull(0) ?: "null")
            append(huBian.getOrNull(0) ?: "null")
        }
    }
}

//fun main() {
//    // 创建GuaCalculator实例
//    val calculator = GuaCalculator2()
//
//    // 定义一个三爻的列表，例如代表乾卦（阳阳阳）
//    val targetValue = listOf(1, 1, 1)
//
//    // 调用toggleElement方法
//    val result = calculator.toggleElement(targetValue)
//    // 打印结果
//    println(result)
//    println(result[1])
//    println(result[2])
//
//}