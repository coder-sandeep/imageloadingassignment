package com.codersandeep.imageloadingassignment.utils.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.codersandeep.imageloadingassignment.R
import java.net.URL

class BitmapCacheHandler(private val context: Context) {
    private val bitmapMemoryCache = BitmapMemoryCache()
    private val bitmapDiskCache = BitmapDiskCache(context)

    fun getImage(url: String, key: String): Bitmap {
        val bitmapFromMemory = bitmapMemoryCache.getBitmapFromMemCache(key)
        if (bitmapFromMemory == null) {
            val bitmapFromDisk = bitmapDiskCache.getBitmapFromDisk(key)
            return if (bitmapFromDisk == null) {
                val (bitmapFromNetwork, isBrokenImage) = getImageFromNetwork(url)
                if (!isBrokenImage) {
                    bitmapMemoryCache.addBitmapToMemoryCache(key, bitmapFromNetwork)
                    bitmapDiskCache.saveBitmapToDisk(key, bitmapFromNetwork)
                }//Saves bitmap to disk and cache if images downloaded from network
                Log.d("cache log", "Network")
                bitmapFromNetwork
            } else {
                bitmapMemoryCache.addBitmapToMemoryCache(
                    key,
                    bitmapFromDisk
                )//Saves bitmap to memory cache if found in disk
                Log.d("cache log", "Disk")
                bitmapFromDisk
            }
        } else {
            if (bitmapDiskCache.getBitmapFromDisk(key) == null)
                bitmapDiskCache.saveBitmapToDisk(
                    key,
                    bitmapFromMemory
                ) //Saves bitmap to disk cache if found in memory
            Log.d("cache log", "Memory")
            return bitmapFromMemory
        }
    }

    private fun getImageFromNetwork(url: String): Pair<Bitmap, Boolean> {
        return try {
            val conn = URL(url).openConnection()
            conn.connect()
            val inputStream = conn.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return Pair(bitmap, false)
        } catch (e: Exception) {
            return Pair(
                BitmapFactory.decodeResource(
                    context.resources, R.drawable.ic_broken_image
                ), true
            ) //returns placeholder if something goes wrong with network image
        }
    }
}