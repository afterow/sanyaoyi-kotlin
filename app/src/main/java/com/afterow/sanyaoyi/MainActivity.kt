package com.afterow.sanyaoyi
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
        val lunarHour = LunarHour.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        )


        val eightChar = lunarHour.eightChar.toString().replace(" ", "")
        val eightCharlist = eightChar.toList()

        binding.niangan.text = eightCharlist.get(0).toString()
        binding.yuegan.text = eightCharlist.get(2).toString()
        binding.rigan.text = eightCharlist.get(4).toString()
        binding.shigan.text = eightCharlist.get(6).toString()

        binding.nianzhi.text = eightCharlist.get(1).toString()
        binding.yuezhi.text = eightCharlist.get(3).toString()
        binding.rizhi.text = eightCharlist.get(5).toString()
        binding.shizhi.text = eightCharlist.get(7).toString()


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        // 页面跳转代码
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}

