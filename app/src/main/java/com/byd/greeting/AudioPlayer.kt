package com.byd.greeting

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast

object AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private const val TAG = "BYDGreeting"

    fun play(context: Context, uri: Uri?, type: String) {
        if (uri == null) {
            Toast.makeText(context, "Chưa chọn file audio cho $type", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Stop previous playback
            stop()

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context, uri)
                setOnPreparedListener {
                    it.start()
                    Log.d(TAG, "Playing $type audio")
                }
                setOnCompletionListener {
                    stop()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what extra=$extra")
                    Toast.makeText(context, "Lỗi phát audio", Toast.LENGTH_SHORT).show()
                    stop()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error playing audio", e)
            Toast.makeText(context, "Không thể phát file: ${e.message}", Toast.LENGTH_SHORT).show()
            stop()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }
}
