package com.afterow.sanyaoyi

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.afterow.sanyaoyi.GuaCalculator


class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val yao1: TextView = findViewById(R.id.yao1)
        val yao2: TextView = findViewById(R.id.yao2)
        val yao3: TextView = findViewById(R.id.yao3)

        // 设置点击事件监听器
        val listener = View.OnClickListener { view ->
            val yaoTextView = view as TextView
            // 切换爻的显示
            yaoTextView.text = if (yaoTextView.text == "⚋") "⚊" else "⚋"
        }

        // 为每个爻设置点击事件
        yao1.setOnClickListener(listener)
        yao2.setOnClickListener(listener)
        yao3.setOnClickListener(listener)



        val myButton: Button = findViewById(R.id.anl1)
        myButton.setOnClickListener {
            val mutableList = mutableListOf<Int>()
            val views = listOf(yao1, yao2, yao3)
            views.forEach {
                if (it.text == "⚊") {
                    mutableList.add(1)
                } else {
                    mutableList.add(0)
                }

            }
            val test100:TextView = findViewById(R.id.test1)
            val guaCalculator = GuaCalculator()
            val targetValue = mutableList
            val result = guaCalculator.toggleElement(targetValue)
            test100.text =result

        }
    }
}