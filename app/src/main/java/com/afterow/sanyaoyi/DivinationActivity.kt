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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

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

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val viewPagerAdapter = ViewPagerAdapter(this);
        viewPager.adapter = viewPagerAdapter

        // 设置保存按钮的点击事件
        var saveButton: Button = findViewById(R.id.save_button2)
        var scrollView1: LinearLayout = findViewById(R.id.scrollView1)  // 需要保存为图片的视图

        saveButton.setOnClickListener {
            // 捕获并显示屏幕截图
            val fragment = viewPagerAdapter.createFragment(viewPager.currentItem)
            val scrollView2: View = fragment.requireView().findViewById(R.id.scrollView2)
            ScreenshotUtils.captureAndShowScreenshot(this, scrollView1, scrollView2, fileName = now.toString())
        }
    }
}

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = listOf(
        Page1Fragment(),
        Page2Fragment(),
        Page3Fragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
