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
        listOf(
            "火天大有",
            "火泽睽",
            "离为火",
            "火雷噬嗑",
            "火风鼎",
            "火水未济",
            "火山旅",
            "火地晋"
        ),
        listOf(
            "雷天大壮",
            "雷泽归妹",
            "雷火丰",
            "震为雷",
            "雷风恒",
            "雷水解",
            "雷山小过",
            "雷地豫"
        ),
        listOf(
            "风天小畜",
            "风泽中孚",
            "风火家人",
            "风雷益",
            "巽为风",
            "风水涣",
            "风山渐",
            "风地观"
        ),
        listOf("水天需", "水泽节", "水火既济", "水雷屯", "水风井", "坎为水", "水山蹇", "水地比"),
        listOf("山天大蓄", "山泽损", "山火贲", "山雷颐", "山风蛊", "山水蒙", "艮为山", "山地剥"),
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


    fun toggleElement(targetValue: List<Int>): List<Any> {
        val tmpList = targetValue?.map { if (it % 2 == 0) 0 else 1 }?.toMutableList() ?: mutableListOf()
        val targetValue = targetValue?.map { if (it % 2 == 0) 0 else 1 }?.toMutableList() ?: mutableListOf()

        // 将输入的整数列表转换为 0 和 1 的形式

        // 计算当前卦象
        val manGua = findKeyByValue(bagua, tmpList) ?: return listOf("本卦未找到")

        // 切换目标值
        val totalSum = tmpList.sum()
        val yaobian = totalSum % 3
        tmpList[yaobian] = if (tmpList[yaobian] == 1) 0 else 1

        // 计算变卦
        val bianGua = findKeyByValue(bagua, tmpList)

        // 合并原始和变更后的列表
        val flattenedList = mutableListOf(targetValue, tmpList).flatten()

        // 获取相关卦象
        val huBen = findKeyByValue(bagua, flattenedList.subList(1, 4))
        val huBian = findKeyByValue(bagua, flattenedList.subList(2, 5))

        // 提取关键值
        val ixman = manGua.getOrNull(2) as Int
        val ixbianGua = bianGua?.getOrNull(2) as Int
        val ixhuBen = huBen?.getOrNull(2) as Int
        val ixhuBian = huBian?.getOrNull(2) as Int

        // 定义获取最后两个字符的函数
        fun takeLastBasedOnLength(input: String): String {
            return input.takeLast(if (input.length >= 4) 2 else 1)
        }

        // 计算各个宫位
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

        // 返回最终结果列表
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
            manGua.getOrNull(0) ?: "null",
            bianGua.getOrNull(0) ?: "null",
            huBen.getOrNull(0) ?: "null",
            huBian.getOrNull(0) ?: "null"
        )
    }
}




    // 使用示例
fun main() {
    val processor = GuaCalculator()
    val results = processor.toggleElement(listOf(9, 8, 0))
    println(results)
}