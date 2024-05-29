package nikhil.com.galleryassignment.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nikhil.com.galleryassignment.ImageLoader
import nikhil.com.galleryassignment.R
import nikhil.com.galleryassignment.adapter.ImageAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var imageLoader: ImageLoader
    private lateinit var recyclerView: RecyclerView
    private val imageUrls = mutableListOf<String>()
    private val client = OkHttpClient()
    private val imageCacheDir: File by lazy { File(cacheDir, "images") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null

        fetchData()
    }

    private fun fetchData() {
        val request = Request.Builder()
            .url("https://acharyaprashant.org/api/v2/content/misc/media-coverages?limit=100")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonData = responseBody.string()
                    Log.d("MainActivity", "Response: $jsonData") // Log the raw JSON response

                    try {
                        val jsonArray = JSONArray(jsonData) // Adjust if root element is an array

                        for (i in 0 until jsonArray.length()) {
                            val dataObject = jsonArray.getJSONObject(i)
                            val thumbnail = dataObject.getJSONObject("thumbnail")
                            val imageUrl = "${thumbnail.getString("domain")}/${thumbnail.getString("basePath")}/0/${thumbnail.getString("key")}"
                            imageUrls.add(imageUrl)
                        }

                        runOnUiThread {
                            recyclerView.adapter = ImageAdapter(imageUrls, imageCacheDir)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        // Optionally show an error message to the user
                    }
                }
            }
        })
    }
}