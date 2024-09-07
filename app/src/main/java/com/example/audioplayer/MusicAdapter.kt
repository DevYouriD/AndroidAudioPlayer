package com.example.audioplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(private val musicList: List<MusicItem>) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicIcon: ImageView = itemView.findViewById(R.id.musicIcon)
        val songTitle: TextView = itemView.findViewById(R.id.songTitle)
        val dateAdded: TextView = itemView.findViewById(R.id.dateAdded)
        val fileSize: TextView = itemView.findViewById(R.id.fileSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicFile = musicList[position]

        // Set the song title
        holder.songTitle.text = musicFile.title

        // Set the date and time added (formatted as you like)
        holder.dateAdded.text = "Date Added: ${musicFile.dateAdded}"

        // Set the file size (formatted)
        holder.fileSize.text = "File Size: ${musicFile.size}"

        // Set the music icon (you can load an actual album art here if available)
        holder.musicIcon.setImageResource(R.drawable.music_note)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}
