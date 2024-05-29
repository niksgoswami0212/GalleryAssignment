package nikhil.com.galleryassignment.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import androidx.collection.LruCache
import nikhil.com.galleryassignment.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader(private val imageViewReference: WeakReference<ImageView>, private val cacheDir: File) : AsyncTask<String, Void, Bitmap?>() {

    companion object {
        private val memoryCache = object : LruCache<String, Bitmap>(4 * 1024 * 1024) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount
            }
        }
    }

    override fun doInBackground(vararg params: String?): Bitmap? {
        val urlString = params[0] ?: return null
        var bitmap: Bitmap? = memoryCache.get(urlString)

        if (bitmap == null) {
            bitmap = loadImageFromDisk(urlString) ?: loadImageFromNetwork(urlString)
            bitmap?.let {
                memoryCache.put(urlString, it)
                saveImageToDisk(urlString, it)
            }
        }
        return bitmap
    }

    private fun loadImageFromNetwork(urlString: String): Bitmap? {
        return try {
            val url = URL(urlString)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadImageFromDisk(urlString: String): Bitmap? {
        val file = File(cacheDir, urlString.hashCode().toString())
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    private fun saveImageToDisk(urlString: String, bitmap: Bitmap) {
        val file = File(cacheDir, urlString.hashCode().toString())
        if (!file.exists()) {
            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result != null) {
            imageViewReference.get()?.setImageBitmap(result)
        } else {
            imageViewReference.get()?.setImageResource(R.drawable.icon_loader) // Placeholder image
        }
    }
}