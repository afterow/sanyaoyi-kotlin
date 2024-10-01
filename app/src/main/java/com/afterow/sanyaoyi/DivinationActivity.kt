package com.afterow.sanyaoyi
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afterow.sanyaoyi.databinding.ActivityDivinationBinding
import com.tyme.lunar.LunarHour
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
        val eightChar2 = SolarTime.fromYmdHms( now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second).lunarHour.eightChar

        binding.fourValuesTextView.text = eightChar2.toString()


    }
}
