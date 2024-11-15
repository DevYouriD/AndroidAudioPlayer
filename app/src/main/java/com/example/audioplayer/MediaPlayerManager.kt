package com.example.audioplayer

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar

class MediaPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var seekBar: SeekBar
    lateinit var handler: Handler
    lateinit var runnable: Runnable

    private var onProgressUpdateListener: ((Int) -> Unit)? = null

    fun initializeMediaPlayer(filePath: String, seekBar: SeekBar) {
        this.seekBar = seekBar
        handler = Handler(Looper.getMainLooper())

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                seekBar.max = duration
            } catch (e: Exception) {
                println("Error initializing MediaPlayer: $e")
            }
        }
    }

    fun setOnProgressUpdateListener(listener: (Int) -> Unit) {
        onProgressUpdateListener = listener
    }

    fun getTotalDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun back() {
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        val newPosition = (currentPosition - 10000).coerceAtLeast(0)
        mediaPlayer?.seekTo(newPosition)
        updateSeekBar()
    }

    fun playPause(playPauseButton: ImageButton) {
        if (isPlaying) {
            mediaPlayer?.pause()
            playPauseButton.setImageResource(R.drawable.circle_play_button)
            handler.removeCallbacks(runnable) // Stop updating the SeekBar
        } else {
            mediaPlayer?.start()
            playPauseButton.setImageResource(R.drawable.circle_pause_button)
            updateSeekBar()
        }
        isPlaying = !isPlaying
    }

    fun forward() {
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        val newPosition = (currentPosition + 10000).coerceAtMost(mediaPlayer?.duration ?: 0)
        mediaPlayer?.seekTo(newPosition)
        updateSeekBar()
    }

    private fun updateSeekBar() {
        mediaPlayer?.let { player ->
            seekBar.progress = player.currentPosition
            onProgressUpdateListener?.invoke(player.currentPosition)
            runnable = Runnable {
                updateSeekBar()
            }
            handler.postDelayed(runnable, 1000)
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun release() {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
        }
    }
}