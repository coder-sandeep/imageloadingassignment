package com.codersandeep.imageloadingassignment.cache

import android.graphics.Bitmap
import android.util.LruCache

class BitmapMemoryCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = maxMemory / 8 //if less memory few images won't be stored

    private val memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            // Return the size of the bitmap in kilobytes
            return bitmap.byteCount / 1024
        }
    }

    // Add bitmap to cache
    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap)
        }
    }

    // Get bitmap from cache
    fun getBitmapFromMemCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }
}