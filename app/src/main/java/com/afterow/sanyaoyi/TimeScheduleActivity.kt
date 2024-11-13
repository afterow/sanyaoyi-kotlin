package com.afterow.sanyaoyi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.afterow.sanyaoyi.utils.LunarUtils
import com.afterow.sanyaoyi.utils.ScreenshotUtils
import com.tyme.eightchar.EightChar
import com.tyme.lunar.LunarHour
import com.tyme.solar.SolarTime
import java.time.LocalDateTime
import java.util.*

class TimeScheduleActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var selectTimeButton: Button
    private lateinit var fourValuesTextView: TextView
    private lateinit var lunarDateTextView: TextView
    private lateinit var gregorianDateTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var scrollView: LinearLayout
    private lateinit var cxkTextView: TextView // 新增的 TextView

    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0
    private var selectedDay: Int = 0
    private var selectedGender: String = "男" // 默认性别为男

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_schedule) // 确保您有相应的布局文件

        try {
            // 初始化视图
            selectTimeButton = findViewById(R.id.selectTimeButton) // 确保初始化
            fourValuesTextView = findViewById(R.id.fourValuesTextView)
            lunarDateTextView = findViewById(R.id.lunarDateTextView)
            gregorianDateTextView = findViewById(R.id.gregorianDateTextView)
            saveButton = findViewById(R.id.save_button)
            scrollView = findViewById(R.id.scrollView)
            cxkTextView = findViewById(R.id.cxk) // 初始化 cxk TextView

            // 设置当前时间的八字和农历信息
            setCurrentDateTimeInfo()

            // 设置选择时间按钮的点击事件
            selectTimeButton.setOnClickListener {
                showGenderSelectionDialog() // 移动性别选择到这里
            }

            // 设置保存按钮的点击事件
            saveButton.setOnClickListener {
                ScreenshotUtils.captureAndShowScreenshot(this, scrollView, fileName = "文件名")
            }
        } catch (e: Exception) {
            e.printStackTrace() // 打印异常信息
        }
    }

    private fun setCurrentDateTimeInfo() {
        val now = LocalDateTime.now()
        val lunarHour = SolarTime.fromYmdHms(
            now.year, now.monthValue, now.dayOfMonth,
            now.hour, now.minute, now.second
        ).lunarHour

        fourValuesTextView.text = lunarHour.eightChar.toString()
        lunarDateTextView.text = lunarHour.toString()
        gregorianDateTextView.text = now.toString()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            this.selectedYear = selectedYear
            this.selectedMonth = selectedMonth + 1 // 月份从0开始
            this.selectedDay = selectedDay
            showTimePicker(selectedYear, selectedMonth + 1, selectedDay)
        }, year, month, day).show()
    }

    private fun showTimePicker(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            this.selectedHour = selectedHour
            this.selectedMinute = selectedMinute
            // 获取选择的性别并调用 LunarUtils
            val (eightChar, lunarHour, results) = LunarUtils.getLunarData(
                selectedYear, selectedMonth, selectedDay,
                selectedHour, selectedMinute, 0, selectedGender
            )
            displayResults(eightChar, lunarHour, results)
        }, hour, minute, true).show()
    }

    private fun showGenderSelectionDialog() {
        val genderOptions = arrayOf("男", "女")
        AlertDialog.Builder(this)
            .setTitle("选择性别")
            .setSingleChoiceItems(genderOptions, 0) { dialog, which ->
                selectedGender = genderOptions[which]
            }
            .setPositiveButton("确定") { dialog, _ ->
                dialog.dismiss()
                showDatePicker() // 在选择性别后显示日期选择器
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun displayResults(eightChar: EightChar, lunarHour: LunarHour, results: List<Any>) {
        // 格式化选择的时间
        val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)

        // 更新 cxk TextView 的内容
        cxkTextView.text = "选择的日期: $selectedDate\n选择的时间: $selectedTime\n性别: $selectedGender\n八字: $eightChar\n农历时辰: $lunarHour\n结果: $results"
    }
}
