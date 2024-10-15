package com.afterow.sanyaoyi

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.afterow.sanyaoyi.Utils.ScreenshotUtils
import com.afterow.sanyaoyi.databinding.ActivityTimeScheduleBinding
import com.tyme.solar.SolarTime
import java.time.LocalDateTime

class TimeScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimeScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimeScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val now = LocalDateTime.now()

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


        val saveButton: Button = findViewById(R.id.save_button)
        val scrollView: LinearLayout = findViewById(R.id.scrollView)  // 需要保存为图片view

        saveButton.setOnClickListener {
            ScreenshotUtils.captureAndShowScreenshot(this, scrollView ,fileName="文件名")


        }
    }
}