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

        binding = ActivityDivinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val now = LocalDateTime.now()

        // 北京时间2005年12月23日，08:37:00转八字
        val eightChar2 = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour.eightChar

        binding.fourValuesTextView.text = eightChar2.toString()

        val lunarHour2 = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour

        binding.lunarDateTextView.text = lunarHour2.toString()

        binding.gregorianDateTextView.text = now.toString()

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN )

        val listString = intent.getStringExtra("listData")

        val mutableList =
            listString?.split(",")?.map { it.toIntOrNull() }?.filterNotNull()?.toMutableList()
                ?: mutableListOf()

        val calculator = GuaCalculator()
        val result = calculator.toggleElement(mutableList, listOf(0, 2)) // 传入动爻索引列表

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

        var saveButton: Button = findViewById(R.id.save_button2)
        var scrollView1: LinearLayout = findViewById(R.id.scrollView1)  // 需要保存为图片view
        var scrollView2: LinearLayout = findViewById(R.id.scrollView2)  // 需要保存为图片view

        saveButton.setOnClickListener {
            ScreenshotUtils.captureAndShowScreenshot(this, scrollView1, scrollView2, fileName = "文件名")
        }
    }
}