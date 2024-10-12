package com.afterow.sanyaoyi
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afterow.sanyaoyi.databinding.ActivityDivinationBinding
import com.tyme.solar.SolarTime
import java.time.LocalDateTime
import com.afterow.sanyaoyi.GuaCalculator2

class DivinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDivinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDivinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val now = LocalDateTime.now()

        // 北京时间2005年12月23日，08:37:00转八字
        val eightChar2 = SolarTime.fromYmdHms( now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second).lunarHour.eightChar

        binding.fourValuesTextView.text = eightChar2.toString()

        val lunarHour2 = SolarTime.fromYmdHms( now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second).lunarHour

        binding.lunarDateTextView.text = lunarHour2.toString()


        binding.gregorianDateTextView.text = now.toString()


       // 主要逻辑

        val calculator = GuaCalculator2()

       // 定义一个三爻的列表，例如代表乾卦（阳阳阳）
        val targetValue = listOf(1, 1, 1)

       // 调用toggleElement方法
        val result = calculator.toggleElement(targetValue)

        val textViews1 = arrayOf(
            binding.gong1, binding.gong2, binding.gong3, binding.gong4,
            binding.gong5, binding.gong6, binding.gong7, binding.gong8,
            binding.gong9, binding.gong10, binding.gong11, binding.gong12
        )

        for (i in textViews1.indices) {
            textViews1[i].text = result[i].toString()
        }

        binding.manGua.text = result[12].toString()
        binding.bianGua.text = result[13].toString()
        binding.huBen.text = result[14].toString()
        binding.huBian.text = result[15].toString()
    }
}
