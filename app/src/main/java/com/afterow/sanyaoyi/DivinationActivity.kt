package com.afterow.sanyaoyi

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.afterow.sanyaoyi.pageFrag.Page1Fragment
import com.afterow.sanyaoyi.pageFrag.Page2Fragment
import com.afterow.sanyaoyi.pageFrag.Page3Fragment
import com.afterow.sanyaoyi.databinding.ActivityDivinationBinding
import com.afterow.sanyaoyi.utils.ScreenshotUtils
import com.tyme.solar.SolarTime
import java.time.LocalDateTime
import kotlin.math.absoluteValue

class DivinationActivity : AppCompatActivity() {

    companion object {
        private const val LEFT_VIBRATION_AMPLITUDE = 255
        private const val RIGHT_VIBRATION_AMPLITUDE = 180
        private val VIBRATION_PATTERN = longArrayOf(0, 80, 50, 60)
    }

    private var previousPosition = -1

    private lateinit var binding: ActivityDivinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        // 初始化视图绑定
        binding = ActivityDivinationBinding.inflate(layoutInflater);
        setContentView(binding.root);

        // 获取当前时间
        val now = LocalDateTime.now();

        // 计算当前时间的八字
        val eightChar2 = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour.eightChar;

        // 将八字显示在界面上
        binding.fourValuesTextView.text = eightChar2.toString();

        // 获取当前时间的农历时辰
        val lunarHour2 = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour;

        // 将农历时辰显示在界面上
        binding.lunarDateTextView.text = lunarHour2.toString();

        // 将公历时间显示在界面上
        binding.gregorianDateTextView.text = now.toString();

        // 设置沉浸式底部导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView);
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        } else {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView);
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        }

        val viewPager = findViewById<ViewPager2>(R.id.viewPager);
        // 获取震动服务
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        // 设置滑动监听
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                
                // 计算滑动方向 0:初始 1:向右 2:向左
                val direction = when {
                    previousPosition == -1 -> 0
                    position > previousPosition -> 2
                    else -> 1
                }
                previousPosition = position

                // 根据方向调整震动强度
                if (vibrator?.hasVibrator() == true) {
                    val amplitudes = when (direction) {
                        2 -> intArrayOf(0, LEFT_VIBRATION_AMPLITUDE, 0, LEFT_VIBRATION_AMPLITUDE/2)
                        else -> intArrayOf(0, RIGHT_VIBRATION_AMPLITUDE, 0, RIGHT_VIBRATION_AMPLITUDE/2)
                    }

                    vibrator.vibrate(VibrationEffect.createWaveform(VIBRATION_PATTERN, amplitudes, -1))
                }
            }

        })
        // 设置过渡动画
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        // 设置滑动速度
        try {
            val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView = recyclerViewField.get(viewPager) as RecyclerView
            val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.isAccessible = true
            val touchSlop = touchSlopField.get(recyclerView) as Int
            touchSlopField.set(recyclerView, touchSlop * 2) // 减慢滑动速度
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        // 设置保存按钮的点击事件
        val saveButton: Button = findViewById(R.id.save_button2)
        val scrollView1: LinearLayout = findViewById(R.id.scrollView1)  // 需要保存为图片的视图

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

// 添加ZoomOutPageTransformer类
class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    private val minScale = 0.85f
    private val minAlpha = 0.5f

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 1 -> {
                    val scaleFactor = minScale.coerceAtLeast(1 - position.absoluteValue)
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    alpha = (minAlpha +
                            (((scaleFactor - minScale) / (1 - minScale)) * (1 - minAlpha)))
                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }
}
