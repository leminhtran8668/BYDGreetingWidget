package com.byd.greeting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.byd.greeting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pickingForStart = true

    private val pickAudioLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                val name = getFileName(uri) ?: "audio_file"
                if (pickingForStart) {
                    Prefs.setStartAudio(this, uri, name)
                    binding.tvStartFile.text = name
                    Toast.makeText(this, "Đã chọn audio Khởi động", Toast.LENGTH_SHORT).show()
                } else {
                    Prefs.setEndAudio(this, uri, name)
                    binding.tvEndFile.text = name
                    Toast.makeText(this, "Đã chọn audio Kết thúc", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (canDrawOverlay()) {
            Toast.makeText(this, "Đã cấp quyền hiển thị trên app khác", Toast.LENGTH_SHORT).show()
            FloatingService.start(this)
            updateFloatButton()
        } else {
            Toast.makeText(this, "Chưa có quyền overlay — panel nổi không chạy được", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvStartFile.text = Prefs.getStartName(this)
        binding.tvEndFile.text = Prefs.getEndName(this)
        binding.switchAutoStart.isChecked = Prefs.isAutoStartFloat(this)
        updateFloatButton()

        binding.switchAutoStart.setOnCheckedChangeListener { _, isChecked ->
            Prefs.setAutoStartFloat(this, isChecked)
            Toast.makeText(
                this,
                if (isChecked) "Sẽ tự mở panel khi boot xe" else "Không tự mở khi boot",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnToggleFloat.setOnClickListener {
            if (Prefs.isFloatRunning(this)) {
                FloatingService.stop(this)
                Prefs.setFloatRunning(this, false)
                updateFloatButton()
                Toast.makeText(this, "Đã tắt panel nổi", Toast.LENGTH_SHORT).show()
            } else {
                requestOverlayAndStart()
            }
        }

        binding.btnPickStart.setOnClickListener {
            pickingForStart = true
            openAudioPicker()
        }
        binding.btnPickEnd.setOnClickListener {
            pickingForStart = false
            openAudioPicker()
        }
        binding.btnClearStart.setOnClickListener {
            Prefs.clearStart(this)
            binding.tvStartFile.text = "Chưa chọn"
        }
        binding.btnClearEnd.setOnClickListener {
            Prefs.clearEnd(this)
            binding.tvEndFile.text = "Chưa chọn"
        }
        binding.btnTestStart.setOnClickListener {
            AudioPlayer.play(this, Prefs.getStartUri(this), "Khởi động")
        }
        binding.btnTestEnd.setOnClickListener {
            AudioPlayer.play(this, Prefs.getEndUri(this), "Kết thúc")
        }
    }

    override fun onResume() {
        super.onResume()
        updateFloatButton()
    }

    private fun updateFloatButton() {
        val running = Prefs.isFloatRunning(this)
        binding.btnToggleFloat.text = if (running) "Tắt panel nổi" else "Bật panel nổi"
        binding.tvFloatStatus.text = if (running) {
            "Trạng thái: ĐANG BẬT (kéo ⋮⋮ để di chuyển)"
        } else {
            "Trạng thái: Đã tắt"
        }
    }

    private fun canDrawOverlay(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else true
    }

    private fun requestOverlayAndStart() {
        if (canDrawOverlay()) {
            FloatingService.start(this)
            updateFloatButton()
            Toast.makeText(this, "Đã bật panel nổi — kéo ⋮⋮ để di chuyển", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Cần cấp quyền \"Hiển thị trên app khác\"", Toast.LENGTH_LONG).show()
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            overlayPermissionLauncher.launch(intent)
        }
    }

    private fun openAudioPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        pickAudioLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String? {
        var name: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }
}
