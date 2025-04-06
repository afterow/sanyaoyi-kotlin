package com.afterow.sanyaoyi

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.afterow.sanyaoyi.utils.GuaCalculator
import com.afterow.sanyaoyi.utils.ScreenshotUtils
import com.afterow.sanyaoyi.databinding.ActivityDivinationBinding
import com.tyme.solar.SolarTime
import java.time.LocalDateTime

class DivinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDivinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化视图绑定
        binding = ActivityDivinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取当前时间
        val now = LocalDateTime.now()

        // 计算当前时间的八字
        val eightChar2 = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour.eightChar

        // 将八字显示在界面上
        binding.fourValuesTextView.text = eightChar2.toString()

        // 获取当前时间的农历时辰
        val lunarHour2 = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour

        // 将农历时辰显示在界面上
        binding.lunarDateTextView.text = lunarHour2.toString()

        // 将公历时间显示在界面上
        binding.gregorianDateTextView.text = now.toString()

        // 设置沉浸式底部导航栏
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )


        // 从 Intent 中获取 "listData" 字段的值
        val listString = intent.getStringExtra("listData")
        val yaobianList =intent.getStringExtra("yaobianList")

        // 将字符串转换为 MutableList<Int>
        val mutableList =
            listString?.split(",")?.map { it.toIntOrNull() }?.filterNotNull()?.toMutableList()
                ?: mutableListOf()

        val yaobianList2 = yaobianList?.split(",")?.map { it.toIntOrNull() }?.filterNotNull()?.toMutableList() ?: mutableListOf()

        // 创建 GuaCalculator 实例并计算卦象
        val calculator = GuaCalculator()
        val result = calculator.toggleElement(mutableList,yaobianList2) // 传入动爻索引列表

        // 将计算结果显示在界面上
        val textViews1 = arrayOf(
            binding.gong1, binding.gong2, binding.gong3, binding.gong4,
            binding.gong5, binding.gong6, binding.gong7, binding.gong8,
            binding.gong9, binding.gong10, binding.gong11, binding.gong12
        )
        binding.manGua.text = result[12].toString()
        binding.bianGua.text = result[13].toString()
        binding.huBen.text = result[14].toString()
        binding.huBian.text = result[15].toString()
        for (i in textViews1.indices) {
            textViews1[i].text = result[i].toString()
        }

        // 设置保存按钮的点击事件
        var saveButton: Button = findViewById(R.id.save_button2)
        var scrollView1: LinearLayout = findViewById(R.id.scrollView1)  // 需要保存为图片的视图
        var scrollView2: LinearLayout = findViewById(R.id.scrollView2)  // 需要保存为图片的视图

        saveButton.setOnClickListener {
            // 捕获并显示屏幕截图
            ScreenshotUtils.captureAndShowScreenshot(this, scrollView1, scrollView2, fileName = now.toString())
        }
    }
}
