package com.example.audioplayer.views

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.MusicAdapter
import com.example.audioplayer.MusicItem
import com.example.audioplayer.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SelectionActivity : AppCompatActivity() {

    // Instance variables
    private lateinit var returnToHomePageButton: ImageButton
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var musicList: MutableList<MusicItem>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        enableEdgeToEdge()

        returnToHomePageButton = findViewById(R.id.returnToHomePageButton)
        recyclerView = findViewById(R.id.musicItemList)

        musicList = mutableListOf()

        recyclerView.layoutManager = LinearLayoutManager(this)
        musicAdapter = MusicAdapter(musicList)
        recyclerView.adapter = musicAdapter

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // List files from the Music directory
        listFilesInMusicDirectory()

        // Button to return to Home
        returnToHomePageButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Modify back-button behavior
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@SelectionActivity, MainActivity::class.java))
                finish()
            }
        })
    }

    // Main functionality
    private fun test() {
        listFilesInMusicDirectory()
        // retrieveMetaData("example.mp3")
    }

    // List all files in the Android music directory
    private fun listFilesInMusicDirectory() {
        try {
            val path = Environment.getExternalStorageDirectory().toString() + "/Music"
            val directory = File(path)

            // List files with allowed audio extensions
            val audioExtensions =
                arrayOf("mp3", "wav", "flac", "aac", "ogg", "m4a", "wma", "aiff", "opus")
            val audioFiles = directory.listFiles { file ->
                file.isFile && audioExtensions.contains(file.extension.lowercase(Locale.ROOT))
            }

            if (audioFiles != null) {
                val initialSize = musicList.size

                for (file in audioFiles) {
                    val fileName = file.name
                    val fileSize = "${file.length() / 1024} KB"
                    val dateModified = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(Date(file.lastModified()))

                    musicList.add(MusicItem(fileName, dateModified, fileSize))
                }
                musicAdapter.notifyItemRangeInserted(initialSize, audioFiles.size)
            } else {
                println("No audio files found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun retrieveMetaData(file: String): Map<String, String?> {
        val metadataRetriever = MediaMetadataRetriever()
        val metadataMap = mutableMapOf<String, String?>()
        val path = Environment.getExternalStorageDirectory().toString() + "/Music/" + file

        try {
            metadataRetriever.setDataSource(path)

            metadataMap["Title"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            metadataMap["Artist"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            metadataMap["Album"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            metadataMap["Genre"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
            metadataMap["Duration (ms)"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            metadataMap["Year"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)
            metadataMap["Album Artist"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
            metadataMap["Composer"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)
            metadataMap["Disc Number"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)
            metadataMap["Track Number"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
            metadataMap["Compilation"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION)
            metadataMap["Writer"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)
            metadataMap["Location"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION)
            metadataMap["MIME Type"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)

            // Optional: Image metadata if applicable
            metadataMap["HasImage"] =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE)

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
}