package com.codersandeep.imageloadingassignment.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.codersandeep.imageloadingassignment.R
import java.lang.Exception
import java.net.URL

class BitmapCacheHandler(private val context: Context) {
    private val bitmapMemoryCache = BitmapMemoryCache()
    private val bitmapDiskCache = BitmapDiskCache(context)

    fun getImage(url: String, key: String): Bitmap {
        val bitmapFromMemory = bitmapMemoryCache.getBitmapFromMemCache(url)
        if (bitmapFromMemory == null) {
            val bitmapFromDisk = bitmapDiskCache.getBitmapFromFile(key)
            return if (bitmapFromDisk == null) {
                val bitmapFromNetwork = getImageFromNetwork(url)
                bitmapMemoryCache.addBitmapToMemoryCache(url, bitmapFromNetwork)
                bitmapDiskCache.saveBitmapToFile(key, bitmapFromNetwork)
                Log.d("ablog", "Network")
                bitmapFromNetwork
            } else {
                bitmapMemoryCache.addBitmapToMemoryCache(key, bitmapFromDisk)
                Log.d("ablog", "Disk")
                bitmapFromDisk
            }
        } else {
            if (bitmapDiskCache.getBitmapFromFile(key) == null)
                bitmapDiskCache.saveBitmapToFile(key, bitmapFromMemory)
            Log.d("ablog", "Memory")
            return bitmapFromMemory
        }
    }

    private fun getImageFromNetwork(url: String): Bitmap {
        return try {
            val conn = URL(url).openConnection()
            conn.connect()
            val inputStream = conn.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmapMemoryCache.addBitmapToMemoryCache(url, bitmap)
            return bitmap
        } catch (e: Exception) {
            BitmapFactory.decodeResource(
                context.resources, R.drawable.ic_broken_image
            ) //returns placeholder if something goes wrong with image
        }
    }
}