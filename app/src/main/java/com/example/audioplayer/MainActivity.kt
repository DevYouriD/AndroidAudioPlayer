package com.example.audioplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileNotFoundException

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : AppCompatActivity() {

    private val permissionName = Manifest.permission.READ_MEDIA_AUDIO
    private var mediaPlayer: MediaPlayer? = null
    private val permissionRequestId = 1
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val singlePermissionBtn = findViewById<ImageButton>(R.id.playButton)
        val myTextView = findViewById<TextView>(R.id.textView2)

        val file = "example.mp3"
        val path = Environment.getExternalStorageDirectory().toString() + "/Music/" + file
        myTextView.text = "No song selected"

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(path)
                prepare()  // Prepare the MediaPlayer synchronously
            } catch (e: Exception) {
                println("Error initializing MediaPlayer: $e")
            }
        }

        singlePermissionBtn.setOnClickListener {
            if (isPermissionGranted()) {
                playPause()
            } else {
                showPermissionRationaleDialog()
            }
        }
    }

    // Main functionality
    private fun playPause() {
        // listFilesInMusicDirectory()
        // retrieveMetaData("example.mp3")

        if (isPlaying) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
        isPlaying = !isPlaying
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionRationaleDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permission Required")
            .setMessage("This app needs access to your media files to play audio. Please grant the permission.")
            .setPositiveButton("OK") { _, _ ->
                requestPermission()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                // Optionally handle the cancellation
            }
            .create()
            .show()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestId)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionRequestId) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                playPause()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                    showPermissionRationaleDialog()
                } else {
                    showAppSettingsDialog()
                }
            }
        }
    }

    private fun showAppSettingsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permission Required")
            .setMessage("You have permanently denied this permission. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                // Optionally handle the cancellation
            }
            .create()
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    // List all files in the Android music directory
    private fun listFilesInMusicDirectory() {
        try {
            val path = Environment.getExternalStorageDirectory().toString() + "/Music"
            println("\nPath: $path")
            val directory = File(path)
            val files = directory.listFiles()
            if (files != null) {
                println("Amount of files in directory: " + files.size)
                for (i in files.indices) {
                    println("FileName:" + files[i].name)
                }
                println("\n")
            } else {
                println("No files found")
            }
        } catch (e: FileNotFoundException) {
            println("Exception $e")
        }
    }

    fun retrieveMetaData(file: String): Map<String, String?> {
        val metadataRetriever = MediaMetadataRetriever()
        val metadataMap = mutableMapOf<String, String?>()
        val path = Environment.getExternalStorageDirectory().toString() + "/Music/" + file

        try {
            metadataRetriever.setDataSource(path)

            metadataMap["Title"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            metadataMap["Artist"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            metadataMap["Album"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            metadataMap["Genre"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
            metadataMap["Duration (ms)"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            metadataMap["Year"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)
            metadataMap["Album Artist"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
            metadataMap["Composer"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)
            metadataMap["Disc Number"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)
            metadataMap["Track Number"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
            metadataMap["Compilation"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION)
            metadataMap["Writer"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)
            metadataMap["Location"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION)
            metadataMap["MIME Type"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)

            // Optional: Image metadata if applicable
            metadataMap["HasImage"] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            metadataRetriever.release()
        }

        for ((key, value) in metadataMap) {
            println("$key: $value")
        }

        return metadataMap
    }

    // Release MediaPlayer resources when activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
