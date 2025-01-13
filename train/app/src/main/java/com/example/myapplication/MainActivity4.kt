package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.net.URLEncoder

data class TrainInfo(
    val trainNumber: String,
    val startStation: String,
    val startTime: String,
    val endStation: String,
    val endTime: String,
    val vehicles: String
)

class MainActivity4 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val extras = intent.extras
        if (extras != null) {
            val station = extras.getString("start_station") ?: ""
            val time = extras.getString("time") ?: ""
            val date = extras.getString("date") ?: ""
            val dateTextView: TextView = findViewById(R.id.date12)
            dateTextView.text = date

            // 使用協程進行網絡請求
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // 獲取火車信息列表
                    val trainInfoAPI = TrainInfoAPI(station, time)
                    val trainInfoList = trainInfoAPI.getTrainInfoList()

                    // 切換到主線程更新 UI
                    withContext(Dispatchers.Main) {
                        // 創建 RecyclerView 的適配器並設置
                        val adapter = TrainInfoAdapter(trainInfoList)
                        recyclerView.adapter = adapter
                    }
                } catch (e: Exception) {
                    // 處理異常情況，例如顯示錯誤信息
                    Log.e("MainActivity4", "Error: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity4, "Failed to fetch data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

    class TrainInfoAPI(station: String, time: String) {
        val client = OkHttpClient()
        val apikey = "a123456789"
        private val url: String
        private val trainInfoList = mutableListOf<TrainInfo>()

        init {
            // 构造请求 URL
            url = "http://10.82.0.10:5001/train_info?station=${URLEncoder.encode(station, "UTF-8")}&time=${URLEncoder.encode(time, "UTF-8")}"

            // 创建请求
            val request = Request.Builder()
                .url(url)
                .addHeader("API-Key", apikey)
                .get() // 使用GET方法
                .build()

            // 发送请求并处理响应
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Failed to fetch data: HTTP response code ${response.code}")
            }

            // 解析响应
            val responseBody = response.body?.string() ?: ""
            if (responseBody.isEmpty()) {
                throw IOException("Response is empty")
            }

            val jsonArray = JSONArray(responseBody)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val trainInfo = TrainInfo(
                    jsonObject.getString("train_number"),
                    jsonObject.getString("start_station"),
                    jsonObject.getString("start_time"),
                    jsonObject.getString("end_station"),
                    jsonObject.getString("end_time"),
                    jsonObject.optString("Vehicles", "N/A")
                )
                trainInfoList.add(trainInfo)
            }
        }

        fun getTrainInfoList(): List<TrainInfo> {
            return trainInfoList
        }
    }


    class TrainInfoAdapter(private val trainInfoList: List<TrainInfo>) :
        RecyclerView.Adapter<TrainInfoAdapter.TrainInfoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainInfoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.stop2_item, parent, false)
            return TrainInfoViewHolder(view)
        }

        override fun onBindViewHolder(holder: TrainInfoViewHolder, position: Int) {
            val trainInfo = trainInfoList[position]
            holder.bind(trainInfo)
        }

        override fun getItemCount(): Int {
            return trainInfoList.size
        }

        class TrainInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val trainNumberTextView: TextView = itemView.findViewById(R.id.train_number1)
            private val startStationTextView: TextView = itemView.findViewById(R.id.start_station2)
            private val startTimeTextView: TextView = itemView.findViewById(R.id.start_time2)
            private val endStationTextView: TextView = itemView.findViewById(R.id.end_station2)
            private val endTimeTextView: TextView = itemView.findViewById(R.id.end_time2)


            fun bind(trainInfo: TrainInfo) {
                trainNumberTextView.text = trainInfo.trainNumber
                startStationTextView.text = trainInfo.startStation
                startTimeTextView.text = trainInfo.startTime
                endStationTextView.text = trainInfo.endStation
                endTimeTextView.text = trainInfo.endTime

            }
        }
    }




