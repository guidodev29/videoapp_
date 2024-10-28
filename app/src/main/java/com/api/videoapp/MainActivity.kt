package com.api.videoapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private var latestVideoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)

        findViewById<Button>(R.id.btnRecordVideo).setOnClickListener {
            if (checkPermissions()) {
                recordVideo()
            } else {
                requestPermissions()
            }
        }

        findViewById<Button>(R.id.btnViewVideos).setOnClickListener {
            startActivity(Intent(this, VideoListActivity::class.java))
        }

        findViewById<Button>(R.id.btnPlayLastVideo).setOnClickListener {
            latestVideoPath?.let {
                videoView.setVideoURI(Uri.parse(it))
                videoView.start()
            } ?: Toast.makeText(this, "No hay videos grabados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 1001)
    }

    private fun recordVideo() {
        val videoFile = createVideoFile()
        val videoUri = FileProvider.getUriForFile(
            this, "${packageName}.provider", videoFile
        )
        latestVideoPath = videoFile.absolutePath

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        }
        startActivityForResult(intent, 1002)
    }

    private fun createVideoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir("Videos")
        return File.createTempFile("VIDEO_${timeStamp}_", ".mp4", storageDir)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recordVideo()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }
}
