package com.api.videoapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class VideoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val videos = getVideosFromStorage()
        if (videos.isNotEmpty()) {
            val adapter = VideoAdapter(this, videos) { videoUri ->
                playVideo(videoUri)
            }
            recyclerView.adapter = adapter
        } else {
            Toast.makeText(this, "No hay videos guardados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getVideosFromStorage(): List<File> {
        val videoDirectory = getExternalFilesDir("Videos")
        return videoDirectory?.listFiles()?.toList() ?: emptyList()
    }

    private fun playVideo(uri: Uri) {
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "video/*")
        }
        startActivity(intent)
    }
}
