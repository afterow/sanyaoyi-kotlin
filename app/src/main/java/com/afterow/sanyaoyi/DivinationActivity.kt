package com.afterow.sanyaoyi
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afterow.sanyaoyi.databinding.ActivityDivinationBinding
import java.text.SimpleDateFormat
import java.util.Date
import com.tyme.lunar.LunarHour
import java.time.LocalDateTime



class DivinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDivinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDivinationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 显示当前时间
        val currentTime = SimpleDateFormat("HH:mm:ss").format(Date())
        binding.gregorianDateTextView.text = currentTime


        val now = LocalDateTime.now()
        val lunarHour = LunarHour.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        )

        val eightChar = lunarHour.eightChar.toString()
        binding.fourValuesTextView.text = eightChar


    }
}
