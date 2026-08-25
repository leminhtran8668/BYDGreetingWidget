package com.byd.greeting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Màn hình chặn trước MainActivity: bắt buộc nhập đúng mã kích hoạt
 * (khóa theo thiết bị) mới cho vào sử dụng app.
 */
class LicenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nếu đã kích hoạt từ trước thì vào thẳng MainActivity, không hỏi lại.
        if (LicenseManager.isActivated(this)) {
            goToMain()
            return
        }

        setContentView(R.layout.activity_license)

        val tvDeviceId = findViewById<TextView>(R.id.tv_device_id)
        val btnCopy = findViewById<Button>(R.id.btn_copy_device_id)
        val etCode = findViewById<EditText>(R.id.et_license_code)
        val btnActivate = findViewById<Button>(R.id.btn_activate)
        val tvStatus = findViewById<TextView>(R.id.tv_license_status)

        val deviceId = LicenseManager.getDeviceId(this)
        tvDeviceId.text = deviceId

        btnCopy.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("device_id", deviceId))
            Toast.makeText(this, "Đã sao chép mã thiết bị", Toast.LENGTH_SHORT).show()
        }

        btnActivate.setOnClickListener {
            val input = etCode.text.toString()
            if (input.isBlank()) {
                tvStatus.text = "Vui lòng nhập mã kích hoạt."
                return@setOnClickListener
            }
            val ok = LicenseManager.tryActivate(this, input)
            if (ok) {
                tvStatus.text = "Kích hoạt thành công!"
                Toast.makeText(this, "Kích hoạt thành công", Toast.LENGTH_SHORT).show()
                goToMain()
            } else {
                tvStatus.text = "Mã kích hoạt không đúng với thiết bị này. Kiểm tra lại hoặc liên hệ người bán."
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
