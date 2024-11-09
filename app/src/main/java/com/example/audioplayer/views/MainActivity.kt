package com.example.audioplayer.views

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.audioplayer.MediaPlayerManager
import com.example.audioplayer.PermissionHelper
import com.example.audioplayer.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : AppCompatActivity() {

    private lateinit var permissionHelper: PermissionHelper
    private lateinit var mediaPlayerManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentSongTitle = findViewById<TextView>(R.id.currentSongTitleTextView)
        val elapsedTimeTextView = findViewById<TextView>(R.id.elapsedTimeTextView)
        val totalDurationTextView = findViewById<TextView>(R.id.totalDurationTextView)

        val collectionButton = findViewById<ImageButton>(R.id.collectionButton)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val playPauseButton = findViewById<ImageButton>(R.id.playButton)
        val forwardButton = findViewById<ImageButton>(R.id.forwardButton)

        val seekBar = findViewById<SeekBar>(R.id.progressBar)

        val file = intent.getStringExtra("songTitle")

        permissionHelper = PermissionHelper(this)
        mediaPlayerManager = MediaPlayerManager()

        if (file != null) {
            val path = Environment.getExternalStorageDirectory().toString() + "/Music/" + file

            // Only display first 10 characters of a song title to prevent visual bugs
            val truncatedTitle = if (file.length > 30) {
                file.take(30) + "..."
            } else {
                file
            }

            currentSongTitle.text = truncatedTitle
            mediaPlayerManager.initializeMediaPlayer(path, seekBar)
            mediaPlayerManager.playPause(playPauseButton)

            val totalDuration = mediaPlayerManager.getTotalDuration()
            totalDurationTextView.text = formatTime(totalDuration)

            mediaPlayerManager.setOnProgressUpdateListener { currentTime ->
                elapsedTimeTextView.text = formatTime(currentTime)
            }

            // Back, Play/Pause, and Forward button behavior
            handleButtonClickWithPermission(backButton) { mediaPlayerManager.back() }
            handleButtonClickWithPermission(playPauseButton) { mediaPlayerManager.playPause(playPauseButton) }
            handleButtonClickWithPermission(forwardButton) { mediaPlayerManager.forward() }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        elapsedTimeTextView.text = formatTime(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    mediaPlayerManager.handler.removeCallbacks(mediaPlayerManager.runnable)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val progress = seekBar?.progress ?: 0
                    mediaPlayerManager.seekTo(progress)
                    mediaPlayerManager.handler.postDelayed(mediaPlayerManager.runnable, 1000)
                }
            })
        }

        // Navigate to selection page
        handleButtonClickWithPermission(collectionButton) {
            mediaPlayerManager.release()
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }

        // Modify back-button behavior
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitPopup(this@MainActivity)
            }
        })
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    private fun handleButtonClickWithPermission(button: ImageButton, action: (ImageButton) -> Unit) {
        button.setOnClickListener {
            if (permissionHelper.isPermissionGranted()) {
                action(button)
            } else {
                permissionHelper.showPermissionRationaleDialog {
                    action(button)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.handlePermissionResult(requestCode, grantResults,
            { mediaPlayerManager.playPause(findViewById(R.id.playButton)) },
            { permissionHelper.showPermissionRationaleDialog {
                mediaPlayerManager.playPause(findViewById(
                R.id.playButton)) }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerManager.release()
    }

    private fun showExitPopup(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.exit_popup, null)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        btnConfirm.setOnClickListener {
            finishAffinity()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
