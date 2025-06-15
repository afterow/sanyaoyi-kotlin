package com.afterow.sanyaoyi
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afterow.sanyaoyi.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import com.tyme.solar.SolarTime
import java.time.LocalDateTime



/**
 * 主界面 Activity，负责显示当前时间、四柱八字信息及导航功能
 * 包含以下主要功能：
 * 1. 实时显示当前系统时间
 * 2. 计算并展示当前时刻的八字信息
 */
class HomeActivity : AppCompatActivity() {

    // ViewBinding 对象
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化视图绑定
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 计算当前时刻的八字信息
        val now = LocalDateTime.now()
        val eightChar = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour.eightChar.toString().replace(" ", "")

        // 拆分八字字符到各个 TextView
        val eightCharlist = eightChar.toList()
        binding.apply {
            niangan.text = eightCharlist[0].toString()  // 年干
            yuegan.text = eightCharlist[2].toString()   // 月干
            rigan.text = eightCharlist[4].toString()   // 日干
            shigan.text = eightCharlist[6].toString()   // 时干
            nianzhi.text = eightCharlist[1].toString()  // 年支
            yuezhi.text = eightCharlist[3].toString()   // 月支
            rizhi.text = eightCharlist[5].toString()    // 日支
            shizhi.text = eightCharlist[7].toString()   // 时支
        }

        // 功能按钮点击事件
        binding.textViewPaiPan.setOnClickListener {
            // 跳转排盘功能界面
            startActivity(Intent(this, SelectActivity::class.java))
        }

        binding.textViewTime.setOnClickListener {
            // 跳转时辰宜忌界面
            startActivity(Intent(this, TimeScheduleActivity::class.java))
        }

        binding.textViewBaoShu.setOnClickListener {
            // 跳转报数排盘功能界面
            startActivity(Intent(this, CountingActivity::class.java))
        }

        // 创建日期和时间格式
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val timeFormat = SimpleDateFormat("HH:mm")

        // 分别获取日期和时间字符串
        val currentDate = dateFormat.format(Date())
        val currentTime = timeFormat.format(Date())

        // 设置到对应的 TextView（假设日期显示在 dateTextView）
        binding.dateTextView.text = currentDate  // 新增日期显示
        binding.timeTextView.text = currentTime  // 修改原有时间显示
    }
}

