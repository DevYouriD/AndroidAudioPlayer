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

        val currentSongTitle = findViewById<TextView>(R.id.textView2)
        val collectionButton = findViewById<ImageButton>(R.id.collectionButton)
        val playPauseButton = findViewById<ImageButton>(R.id.playButton)
        val seekBar = findViewById<SeekBar>(R.id.progressBar)

        permissionHelper = PermissionHelper(this)
        mediaPlayerManager = MediaPlayerManager()

        val file = "Example.mp3"
        val path = Environment.getExternalStorageDirectory().toString() + "/Music/" + file
        currentSongTitle.text = file

        mediaPlayerManager.initializeMediaPlayer(path, seekBar)

        playPauseButton.setOnClickListener {
            if (permissionHelper.isPermissionGranted()) {
                mediaPlayerManager.playPause(playPauseButton)
            } else {
                permissionHelper.showPermissionRationaleDialog {
                    mediaPlayerManager.playPause(playPauseButton)
                }
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayerManager.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mediaPlayerManager.handler.removeCallbacks(mediaPlayerManager.runnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaPlayerManager.seekTo(seekBar?.progress ?: 0)
                mediaPlayerManager.handler.postDelayed(mediaPlayerManager.runnable, 1000)
            }
        })

        collectionButton.setOnClickListener {
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitPopup(this@MainActivity)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.handlePermissionResult(requestCode, grantResults,
            { mediaPlayerManager.playPause(findViewById(R.id.playButton)) },
            { permissionHelper.showPermissionRationaleDialog { mediaPlayerManager.playPause(findViewById(
                R.id.playButton
            )) } }
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

