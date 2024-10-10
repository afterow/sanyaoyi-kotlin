package com.afterow.sanyaoyi
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afterow.sanyaoyi.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import com.tyme.lunar.LunarHour
import com.tyme.solar.SolarTime
import java.time.LocalDateTime



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 显示当前时间
        val currentTime = SimpleDateFormat("HH:mm:ss").format(Date())
        binding.timeTextView.text = currentTime


        val now = LocalDateTime.now()

        val eightChar = SolarTime.fromYmdHms( now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second).lunarHour.eightChar.toString().replace(" ", "")

        val eightCharlist = eightChar.toList()

        binding.niangan.text = eightCharlist.get(0).toString()
        binding.yuegan.text = eightCharlist.get(2).toString()
        binding.rigan.text = eightCharlist.get(4).toString()
        binding.shigan.text = eightCharlist.get(6).toString()

        binding.nianzhi.text = eightCharlist.get(1).toString()
        binding.yuezhi.text = eightCharlist.get(3).toString()
        binding.rizhi.text = eightCharlist.get(5).toString()
        binding.shizhi.text = eightCharlist.get(7).toString()

        // 通过binding对象获取按钮并设置点击事件监听器
        binding.textViewPaiPan.setOnClickListener {
            // 创建Intent来启动SecondActivity
            val intent = Intent(this, MainActivity2::class.java)
            // 开始跳转
            startActivity(intent)
        }

        binding.textViewTime.setOnClickListener {
            // 创建Intent来启动SecondActivity
            // 时间排盘
            val intent = Intent(this, DivinationActivity::class.java)
            intent.putExtra("name", "张三");

            // 开始跳转
            startActivity(intent)
        }


    }
}

