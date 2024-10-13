package com.afterow.sanyaoyi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

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

            val listString = mutableList.joinToString(",") { it.toString() }

            val intent = Intent(this, DivinationActivity::class.java)
            intent.putExtra("listData", listString)
            startActivity(intent)
        }



    }
}


