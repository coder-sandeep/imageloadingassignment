package com.codersandeep.imageloadingassignment.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class BitmapDiskCache(context: Context) {
    private val cacheDir: File = File(context.cacheDir, "images")

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    companion object {
        private var instance: BitmapDiskCache? = null

        fun getInstance(context: Context): BitmapDiskCache {
            return instance ?: synchronized(this) {
                instance ?: BitmapDiskCache(context).also { instance = it }
            }
        }
    }

    fun saveBitmapToDisk(key: String, bitmap: Bitmap) {
        val file = File(cacheDir, key)
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            Log.e("DiskCacheManager", "Error saving bitmap to disk cache: ${e.message}")
        }
    }

    fun getBitmapFromDisk(key: String): Bitmap? {
        val file = File(cacheDir, key)
        if (file.exists()) {
            try {
                val fis = FileInputStream(file)
                val bitmap = BitmapFactory.decodeStream(fis)
                fis.close()
                return bitmap
            } catch (e: IOException) {
                Log.e("DiskCacheManager", "Error reading bitmap from disk cache: ${e.message}")
            }
        }
        return null
    }
}

