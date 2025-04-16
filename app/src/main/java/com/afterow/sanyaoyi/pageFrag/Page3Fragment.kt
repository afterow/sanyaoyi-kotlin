package com.afterow.sanyaoyi.pageFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afterow.sanyaoyi.databinding.FragmentPage3Binding
import com.afterow.sanyaoyi.utils.GuaCalculator

class Page3Fragment : Fragment() {

    private lateinit var binding: FragmentPage3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPage3Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 从 Intent 中获取 "listData" 字段的值
        val listString = requireActivity().intent.getStringExtra("listData")
        val yaobianList = requireActivity().intent.getStringExtra("yaobianList")

        // 将字符串转换为 MutableList<Int>
        val mutableList =
            listString?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableList()
                ?: mutableListOf()

        val yaobianList2 = yaobianList?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableList() ?: mutableListOf()

        // 创建 GuaCalculator 实例并计算卦象
        val calculator = GuaCalculator()
        val result = calculator.toggleElement(mutableList,yaobianList2) // 传入动爻索引列表

        // 将计算结果显示在界面上
        val textViews1 = arrayOf(
            binding.gong1, binding.gong2, binding.gong3, binding.gong4,
            binding.gong5, binding.gong6, binding.gong7, binding.gong8,
            binding.gong9, binding.gong10, binding.gong11, binding.gong12
        )
        binding.manGua.text = result[12]
        binding.bianGua.text = result[13]
        binding.huBen.text = result[14]
        binding.huBian.text = result[15]
        for (i in textViews1.indices) {
            textViews1[i].text = result[i]
        }
    }
}