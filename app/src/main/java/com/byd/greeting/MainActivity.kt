package com.byd.greeting

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.byd.greeting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pickingForStart = true
    private var popupDialog: Dialog? = null

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
        binding.switchShowPopup.isChecked = Prefs.isShowPopup(this)

        // Toggle popup
        binding.switchShowPopup.setOnCheckedChangeListener { _, isChecked ->
            Prefs.setShowPopup(this, isChecked)
            Toast.makeText(
                this,
                if (isChecked) "Đã bật popup khi mở app" else "Đã tắt popup khi mở app",
                Toast.LENGTH_SHORT
            ).show()
        }

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

        // Manual show popup
        binding.btnShowPopup.setOnClickListener {
            showGreetingPopup()
        }

        // Auto show popup if enabled
        if (Prefs.isShowPopup(this)) {
            binding.root.post {
                showGreetingPopup()
            }
        }
    }

    private fun showGreetingPopup() {
        if (popupDialog?.isShowing == true) return

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_greeting)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnStart = dialog.findViewById<Button>(R.id.dialog_btn_start)
        val btnEnd = dialog.findViewById<Button>(R.id.dialog_btn_end)
        val btnClose = dialog.findViewById<Button>(R.id.dialog_btn_close)

        btnStart.setOnClickListener {
            AudioPlayer.play(this, Prefs.getStartUri(this), "Khởi động")
        }

        btnEnd.setOnClickListener {
            AudioPlayer.play(this, Prefs.getEndUri(this), "Kết thúc")
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            popupDialog = null
        }

        popupDialog = dialog
        dialog.show()

        // Make dialog wider on car screens
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
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

    override fun onDestroy() {
        popupDialog?.dismiss()
        super.onDestroy()
    }
}
