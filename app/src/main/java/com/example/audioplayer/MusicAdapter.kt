package com.example.audioplayer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.views.MainActivity

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

        // Set list item attributes
        holder.songTitle.text = musicFile.title
        holder.dateAdded.text = "Date Added: ${musicFile.dateAdded}"
        holder.fileSize.text = "File Size: ${musicFile.size}"
        holder.musicIcon.setImageResource(R.drawable.music_note)

        // Navigate to main and start playing selected song on click
        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("songTitle", musicFile.title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}
