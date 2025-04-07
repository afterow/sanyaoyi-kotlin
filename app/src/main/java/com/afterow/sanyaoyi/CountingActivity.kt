package com.afterow.sanyaoyi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class CountingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counting)

        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val editText1: EditText = findViewById(R.id.editText1)
            val editText2: EditText = findViewById(R.id.editText2)
            val editText3: EditText = findViewById(R.id.editText3)

            // 转换输入为有效整数列表
            val validInputs = listOf(
                editText1.text.toString().toIntOrNull(),
                editText2.text.toString().toIntOrNull(),
                editText3.text.toString().toIntOrNull()
            ).mapNotNull { input ->
                input?.let {
                    if (it % 2 == 0) 0 else 1 // 偶数转0，奇数转1
                }
            }

            // 必须三个有效输入
            if (validInputs.size == 3) {
                // 计算爻变索引（确保索引在0-2之间）
                val dynamicIndex = abs(validInputs.sum()) % 3
                val yaobianList = listOf(dynamicIndex)

                // 传递数据到下一个界面
                Intent(this, DivinationActivity::class.java).apply {
                    putExtra("listData", validInputs.joinToString(","))
                    putExtra("yaobianList", yaobianList.joinToString(","))
                    startActivity(this)
                }
            } else {
                Toast.makeText(
                    this,
                    "请输入三个有效数字",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
