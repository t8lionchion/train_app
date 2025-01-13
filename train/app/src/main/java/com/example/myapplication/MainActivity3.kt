package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

data class TrainRoute(
    val startStation: String,
    val startTime: String,
    val endStation: String,
    val endTime: String,
    val trainNumber: Int
)

class MainActivity3 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrainRouteAdapter
    private val trainRoutes: MutableList<TrainRoute> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TrainRouteAdapter(trainRoutes)
        recyclerView.adapter = adapter

        // 从 Intent 中获取数据
        val extras = intent.extras
        if (extras != null) {
            val startStation = extras.getString("start_station") ?: ""
            val endStation = extras.getString("end_station") ?: ""
            val time = extras.getString("time") ?: ""

            // 调用 API 函数，并在回调中更新 RecyclerView
            getTrainRoutes(startStation, endStation, time)
        }
    }

    class TrainRouteAPI {
        private val client = OkHttpClient()

        fun getTrainRoutes(
            startStation: String,
            endStation: String,
            specifiedTime: String,
            callback: Callback
        ) {
            val url = "http://10.82.0.10:5002/api/search"
            val json = JSONObject()
                .put("start_station", startStation)
                .put("end_station", endStation)
                .put("specified_time", specifiedTime)
            val api_key = "b123456789"  // 添加API密钥
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = RequestBody.create(mediaType, json.toString())
            val request = Request.Builder()
                .url(url)
                .addHeader("API-Key", api_key)  // 添加API密钥到请求头
                .post(body)
                .build()

            Log.d("TrainRouteAPI", "Request: $json")
            client.newCall(request).enqueue(callback)
        }
    }

    // 调用 API 函数，获取火车路线信息
    private fun getTrainRoutes(startStation: String, endStation: String, time: String) {
        val api = TrainRouteAPI()
        api.getTrainRoutes(startStation, endStation, time, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity3, "API 请求失败：${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("MainActivity3", "API 请求失败：${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity3, "API 请求失败：${response.message}", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("MainActivity3", "API 请求失败：${response.message}")
                    return
                }

                val responseBody = response.body?.string()
                Log.d("MainActivity3", "Response: $responseBody")

                if (responseBody != null) {
                    try {
                        val jsonArray = JSONArray(responseBody)
                        val routes = mutableListOf<TrainRoute>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val route = TrainRoute(
                                startStation = jsonObject.getString("start_station"),
                                startTime = jsonObject.getString("start_time"),
                                endStation = jsonObject.getString("end_station"),
                                endTime = jsonObject.getString("end_time"),
                                trainNumber = jsonObject.getInt("train_number")
                            )
                            routes.add(route)
                        }
                        runOnUiThread {
                            updateTrainRoutes(routes)
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity3, "解析响应时出错：${e.message}", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("MainActivity3", "解析响应时出错：${e.message}")
                    }
                }
            }
        })
    }

    // 更新 RecyclerView 中的火车路线信息
    private fun updateTrainRoutes(routes: List<TrainRoute>) {
        trainRoutes.clear()
        trainRoutes.addAll(routes)
        adapter.notifyDataSetChanged()
    }

    // 创建 RecyclerView 的适配器
    class TrainRouteAdapter(private val trainRoutes: List<TrainRoute>) :
        RecyclerView.Adapter<TrainRouteAdapter.TrainRouteViewHolder>() {

        class TrainRouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val startStationTextView: TextView = itemView.findViewById(R.id.start_station)
            val startTimeTextView: TextView = itemView.findViewById(R.id.start_time)
            val endStationTextView: TextView = itemView.findViewById(R.id.end_station)
            val endTimeTextView: TextView = itemView.findViewById(R.id.end_time)
            val trainNumberTextView: TextView = itemView.findViewById(R.id.train_number)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainRouteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_item, parent, false)
            return TrainRouteViewHolder(view)
        }

        override fun onBindViewHolder(holder: TrainRouteViewHolder, position: Int) {
            val route = trainRoutes[position]
            holder.startStationTextView.text = route.startStation
            holder.startTimeTextView.text = route.startTime
            holder.endStationTextView.text = route.endStation
            holder.endTimeTextView.text = route.endTime
            holder.trainNumberTextView.text = route.trainNumber.toString()
        }

        override fun getItemCount(): Int {
            return trainRoutes.size
        }
    }
}
