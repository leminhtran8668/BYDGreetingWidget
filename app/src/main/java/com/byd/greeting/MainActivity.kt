package com.byd.greeting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load current selections
        binding.tvStartFile.text = Prefs.getStartName(this)
        binding.tvEndFile.text = Prefs.getEndName(this)

        // Pick Start audio
        binding.btnPickStart.setOnClickListener {
            pickingForStart = true
            openAudioPicker()
        }

        // Pick End audio
        binding.btnPickEnd.setOnClickListener {
            pickingForStart = false
            openAudioPicker()
        }

        // Clear buttons
        binding.btnClearStart.setOnClickListener {
            Prefs.clearStart(this)
            binding.tvStartFile.text = "Chưa chọn"
            Toast.makeText(this, "Đã xóa audio Khởi động", Toast.LENGTH_SHORT).show()
        }

        binding.btnClearEnd.setOnClickListener {
            Prefs.clearEnd(this)
            binding.tvEndFile.text = "Chưa chọn"
            Toast.makeText(this, "Đã xóa audio Kết thúc", Toast.LENGTH_SHORT).show()
        }

        // Test play buttons
        binding.btnTestStart.setOnClickListener {
            AudioPlayer.play(this, Prefs.getStartUri(this), "Khởi động")
        }

        binding.btnTestEnd.setOnClickListener {
            AudioPlayer.play(this, Prefs.getEndUri(this), "Kết thúc")
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
