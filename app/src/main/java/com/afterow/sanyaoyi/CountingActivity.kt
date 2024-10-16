package com.afterow.sanyaoyi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CountingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counting)  // 确保这一行存在，并指向正确的布局文件

        val myButton: Button = findViewById(R.id.addButton)
        myButton.setOnClickListener {
            val editText1: EditText = findViewById(R.id.editText1)
            val editText2: EditText = findViewById(R.id.editText2)
            val editText3: EditText = findViewById(R.id.editText3)

            var inputs: List<Int?> = listOf(
                editText1.text.toString().toIntOrNull(),
                editText2.text.toString().toIntOrNull(),
                editText3.text.toString().toIntOrNull()
            )

            val mutableList = mutableListOf<Int>()
            for (input in inputs) {
                // 检查 input 是否非空
                if (input != null) {
                    // 判断是奇数还是偶数
                    if (input % 2 == 0) {
                        mutableList.add(0) // 偶数
                    } else {
                        mutableList.add(1) // 奇数
                    }
                }
            }

            val validInputs = mutableList.filterNotNull()

            if (validInputs.size > 2) {  // 确保至少有一个有效的输入
                val listString = validInputs.joinToString(",") { it.toString() }


                val intent = Intent(this, DivinationActivity::class.java)
                intent.putExtra("listData", listString)
                startActivity(intent)
            } else {
                // 可以添加一些错误提示，比如Toast
                Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show()
            }
        }
    }
}