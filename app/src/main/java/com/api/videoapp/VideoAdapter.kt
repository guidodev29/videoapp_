package com.api.videoapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class VideoAdapter(
    private val context: Context,
    private val videos: List<File>,
    private val onVideoSelected: (Uri) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoName: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_1, parent, false
        )
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoFile = videos[position]
        holder.videoName.text = videoFile.name

        holder.itemView.setOnClickListener {
            val videoUri = Uri.fromFile(videoFile)
            onVideoSelected(videoUri)
        }
    }

    override fun getItemCount(): Int = videos.size
}
