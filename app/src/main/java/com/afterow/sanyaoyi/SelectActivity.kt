package com.afterow.sanyaoyi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SelectActivity : AppCompatActivity() {

    private lateinit var yao1: TextView
    private lateinit var yao2: TextView
    private lateinit var yao3: TextView
    private lateinit var myButton: Button
    private val yaobianList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        // 初始化爻的 TextView
        yao1 = findViewById(R.id.yao1)
        yao2 = findViewById(R.id.yao2)
        yao3 = findViewById(R.id.yao3)
        myButton = findViewById(R.id.anl1)

        // 设置爻的点击事件监听器
        val listener = View.OnClickListener { view ->
            val yaoTextView = view as TextView
            // 切换爻的显示
            yaoTextView.text = if (yaoTextView.text == "⚋") "⚊" else "⚋"
        }

        // 为每个爻设置点击事件
        yao1.setOnClickListener(listener)
        yao2.setOnClickListener(listener)
        yao3.setOnClickListener(listener)

        // 初始化动爻按钮
        val dongyao1: Button = findViewById(R.id.dongyao1)
        val dongyao2: Button = findViewById(R.id.dongyao2)
        val dongyao3: Button = findViewById(R.id.dongyao3)

        // 设置动爻按钮的点击事件监听器
        val dongyaoListener = View.OnClickListener { view ->
            val button = view as Button
            val index = when (button.id) {
                R.id.dongyao1 -> 0
                R.id.dongyao2 -> 1
                R.id.dongyao3 -> 2
                else -> -1
            }
            if (index != -1) {
                if (yaobianList.contains(index)) {
                    yaobianList.remove(index)
                    button.text = "◯"
                } else {
                    yaobianList.add(index)
                    button.text = "⊗"
                }
            }
        }

        // 为每个动爻按钮设置点击事件
        dongyao1.setOnClickListener(dongyaoListener)
        dongyao2.setOnClickListener(dongyaoListener)
        dongyao3.setOnClickListener(dongyaoListener)

        // 设置 myButton 的点击事件
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

            val listString = mutableList.joinToString(",") { it.toString() }

            val intent = Intent(this, DivinationActivity::class.java)
            intent.putExtra("listData", listString)
            intent.putExtra("yaobianList", yaobianList.joinToString(","))
            startActivity(intent)
        }
    }
}
