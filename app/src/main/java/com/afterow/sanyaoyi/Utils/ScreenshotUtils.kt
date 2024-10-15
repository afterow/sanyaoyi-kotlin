package com.afterow.sanyaoyi.Utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.provider.MediaStore
import android.view.View

object ScreenshotUtils {

    // 使用可变参数来支持一个或多个 View
    fun captureAndShowScreenshot(activity: AppCompatActivity, vararg views: View, fileName: String) {
        val bitmap = createBitmapFromViews(*views) // 使用展开运算符传递 View 数组
        showImageDialog(activity, bitmap, fileName)
    }

    // 修改为接受可变数量的 View
    private fun createBitmapFromViews(vararg views: View): Bitmap {
        // 计算 Bitmap 的总宽度和高度
        var totalWidth = 0
        var totalHeight = 0
        views.forEach { view ->
            totalWidth = Math.max(totalWidth, view.width)
            totalHeight += view.height
        }

        // 创建一个足够大的 Bitmap 来容纳所有 View
        val bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 依次绘制每个 View，注意调整 y 坐标以避免重叠
        var currentY = 0
        views.forEach { view ->
            canvas.translate(0f, currentY.toFloat()) // 移动到当前 y 位置
            view.draw(canvas)
            currentY += view.height // 更新 y 位置
        }

        return bitmap
    }

    private fun showImageDialog(activity: AppCompatActivity, bitmap: Bitmap, fileName: String) {
        val imageView = ImageView(activity).apply {
            setImageBitmap(bitmap)
            // 调整 ImageView 的布局参数以适应 Bitmap 大小（如果需要）
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        AlertDialog.Builder(activity)
            .setTitle("生成的图片")
            .setView(imageView)
            .setPositiveButton("分享") { _, _ -> shareImage(activity, bitmap, fileName) }
            .setNegativeButton("保存") { _, _ -> saveImageToGallery(activity, bitmap, fileName) }
            .setNeutralButton("关闭") { dialog, _ ->
                dialog.dismiss()
                bitmap.recycle() // 释放 Bitmap 资源
            }
            .show()
    }

    private fun shareImage(activity: AppCompatActivity, bitmap: Bitmap, fileName: String) {
        // 使用 ContentResolver 插入图片并获取 URI
        val uri: Uri? = saveImageToGallery(activity, bitmap, fileName)

        if (uri != null) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "分享我的图片")
            }

            if (shareIntent.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(Intent.createChooser(shareIntent, "分享图片"))
            } else {
                Toast.makeText(activity, "没有可用的应用来分享图片", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "无法保存图片", Toast.LENGTH_SHORT).show()
        }

        bitmap.recycle() // 释放 Bitmap 资源
    }

    private fun saveImageToGallery(activity: AppCompatActivity, bitmap: Bitmap, fileName: String): Uri? {
        // 创建一个新的图像文件
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName ${System.currentTimeMillis()}")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyAppImages") // Android 10+ 的 Scoped Storage
        }

        // 将图片插入 MediaStore
        val uri = activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            activity.contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // 压缩并写入输出流
                Toast.makeText(activity, "图片已保存至相册", Toast.LENGTH_SHORT).show() // 提示用户已保存
            }
        }

        return uri
    }
}